package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
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
    private lateinit var tvUploadImage: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnUpdateProfile: Button

    companion object {
        private const val TAG: String = "UpdateProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        sessionManager = SessionManager(applicationContext)
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        btnProfile = findViewById(R.id.btnProfile)
        tvUploadImage = findViewById(R.id.tvUploadImage)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        val mSpannableString = SpannableString(tvUploadImage.text)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        tvUploadImage.text = mSpannableString

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

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
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