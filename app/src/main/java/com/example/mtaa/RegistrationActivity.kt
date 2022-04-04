package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mtaa.api.ApiClient
import com.example.mtaa.data.SessionManager
import com.example.mtaa.data.model.User
import com.example.mtaa.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    companion object {
        private const val TAG: String = "RegistrationActivity"
        private const val minPasswordLength: Int = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sessionManager = SessionManager(this)

        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        btnRegister.setOnClickListener {
            // get and validate user input
            // email
            val email: String = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                return@setOnClickListener
            }
            val emailPattern: Regex = "^[.\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
            if (!email.matches(emailPattern)) {
                etEmail.error = "Email is not valid"
                return@setOnClickListener
            }

            // password
            val password: String = etPassword.text.toString().trim()
            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < minPasswordLength) {
                etPassword.error = "Password must be at least $minPasswordLength characters"
                return@setOnClickListener
            }
            val confirmPassword: String = etConfirmPassword.text.toString().trim()
            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Confirm Password is required"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                etConfirmPassword.error = "Password does not match"
                return@setOnClickListener
            }

            // register user
            val newUser = User(email, password)
            registerUser(newUser)
        }
    }

    private fun registerUser(newUser: User) {
        ApiClient.getApiService(this@RegistrationActivity)
            .registerUser(newUser)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Error: ${t.message}", Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val registeredUser: UserResponse? = response.body()
                        if (registeredUser != null) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "User registered successfully", Toast.LENGTH_LONG
                            ).show()

                            // save user to shared preferences
                            sessionManager.saveUserId(registeredUser.id)
                            sessionManager.saveUserEmail(registeredUser.email)

                            // go to home activity
                            val intent =
                                Intent(this@RegistrationActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Log.d(
                            TAG,
                            "onResponse: ${response.code()} ${response.errorBody()!!.string()}"
                        )
                    }
                }
            })
    }

}