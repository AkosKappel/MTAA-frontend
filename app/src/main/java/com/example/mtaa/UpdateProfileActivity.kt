package com.example.mtaa

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.UserResponse
import com.example.mtaa.models.UserToRegister
import com.example.mtaa.storage.SessionManager
import com.example.mtaa.utilities.Utils
import com.example.mtaa.utilities.Validator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

//        val mSpannableString = SpannableString(tvUploadImage.text)
//        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
//        tvUploadImage.text = mSpannableString

        // fill user data
        etEmail.setText(sessionManager.fetchUserEmail())

        btnLoadPicture.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

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

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }

        btnUploadImage.setOnClickListener {
            if (imageUri != null) {
                val inputData =
                    imageUri?.let { it1 -> contentResolver.openInputStream(it1)?.readBytes() }
                if (inputData != null) {
//                    updateImage(Image(inputData))
                }
            }
        }
    }

//    private fun uploadFile(fileUri: Uri) {
//
//        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
//        // use the FileUtils to get the actual file by uri
//        val file: File = FileUtils.getFile(this, fileUri)
//
//        // create RequestBody instance from file
//        val requestFile: RequestBody = RequestBody.create(
//            MediaType.parse(contentResolver.getType(fileUri)),
//            file
//        )
//
//        // MultipartBody.Part is used to send also the actual file name
//        val body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile)
//
//        // add another part within the multipart request
//        val descriptionString = "hello, this is description speaking"
//        val description = RequestBody.create(
//            MultipartBody.FORM, descriptionString
//        )
//
//        // finally, execute the request
//        ApiClient.getApiService(applicationContext)
//            .uploadIMG(body)
//            .enqueue(object : Callback<Void?> {
//            override fun onResponse(
//                call: Call<Void?>,
//                response: Response<Void?>
//            ) {
//                Log.v("Upload", "success")
//            }
//
//            override fun onFailure(call: Call<Void?>, t: Throwable) {
//                Log.e("Upload error:", t.message!!)
//            }
//        })
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

//    private fun updateImage(inputData: Image) {
//        ApiClient.getApiService(applicationContext)
//            .uploadIMG(inputData)
//            .enqueue(object : Callback<Void> {
//                override fun onFailure(call: Call<Void>, t: Throwable) {
//                    handleFailure(t)
//                }
//
//                override fun onResponse(
//                    call: Call<Void>, response: Response<Void>
//                ) {
//                    if (response.isSuccessful) {
//                        handleSuccessfulResponseIMG(response)
//                    } else {
//                        handleNotSuccessfulResponseIMG(response)
//                    }
//                }
//            })
//    }

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

    private fun handleSuccessfulResponseIMG(response: Response<Void>) {
        val msg = "Meeting deleted successfully"
        Log.d(TAG, msg)
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleNotSuccessfulResponseIMG(response: Response<Void>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        Toast.makeText(applicationContext, "Error: $detail", Toast.LENGTH_LONG).show()
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