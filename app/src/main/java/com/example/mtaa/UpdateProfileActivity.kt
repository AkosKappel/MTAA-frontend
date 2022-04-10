package com.example.mtaa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.UserResponse
import com.example.mtaa.models.UserToRegister
import com.example.mtaa.storage.SessionManager
import com.example.mtaa.utilities.URIPathHelper
import com.example.mtaa.utilities.Utils
import com.example.mtaa.utilities.Validator
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // toolbar elements
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    // window elements
    private lateinit var btnUploadImage: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnUpdateProfile: Button

    // image upload elements
    private lateinit var imageView: ImageView
    private lateinit var btnLoadPicture: Button
    private var imageUri: Uri? = null

    companion object {
        private const val TAG: String = "UpdateProfileActivity"
        private const val PICK_IMAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        imageView = findViewById(R.id.imageView)
        btnLoadPicture = findViewById(R.id.buttonLoadPicture)

        sessionManager = SessionManager(applicationContext)
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        btnProfile = findViewById(R.id.btnProfile)
        btnUploadImage = findViewById(R.id.buttonUploadPicture)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        // fill user data
        etEmail.setText(sessionManager.fetchUserEmail())

        btnUpdateProfile.setOnClickListener {
            if (!Validator.validateEmail(etEmail)) {
                return@setOnClickListener
            }
            val password = etPassword.text.toString().trim()
            if (password.isNotEmpty() && !Validator.validatePassword(etPassword)) {
                return@setOnClickListener
            }
            val email = etEmail.text.toString().trim()
            val updatedUser = UserToRegister(email, password)
            updateUser(updatedUser)
        }

        btnLoadPicture.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

        btnUploadImage.setOnClickListener {
            if (imageUri != null) {
                val inputData =
                    imageUri?.let { contentResolver.openInputStream(it)?.readBytes() }
                if (inputData != null) {
                    permission()
                }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        btnHome.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage() {
        val uriPathHelper = URIPathHelper()
        val path = imageUri?.let { uriPathHelper.getPath(applicationContext, it) }!!

        val file = File(path)

        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)

        val body = MultipartBody.Part.createFormData("image", file.name, requestBody)

        ApiClient.getApiService(applicationContext)
            .uploadIMG(body)
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<Void>, response: Response<Void>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponseImage(response)
                    } else {
                        handleNotSuccessfulResponseImage(response)
                    }
                }
            })
    }

    private fun permission(){
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                uploadImage()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    private fun handleSuccessfulResponseImage(response: Response<Void>) {
        val msg = "Image has been updated"
        Log.d(TAG, msg)
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleNotSuccessfulResponseImage(response: Response<Void>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        Toast.makeText(applicationContext, "Error: $detail", Toast.LENGTH_LONG).show()
    }

    private fun updateUser(user: UserToRegister) {
        ApiClient.getApiService(applicationContext)
            .updateUser(user)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<UserResponse>, response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun handleSuccessfulResponse(response: Response<UserResponse>) {
        val updatedUser: UserResponse? = response.body()
        if (updatedUser != null) {
            sessionManager.saveUserEmail(updatedUser.email)
            val msg = "Profile updated successfully"
            Log.d(TAG, msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(applicationContext, "Profile update failed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun handleNotSuccessfulResponse(response: Response<UserResponse>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        Toast.makeText(applicationContext, "Error: $detail", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }
}