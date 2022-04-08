package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.storage.SessionManager
import com.example.mtaa.models.UserToRegister
import com.example.mtaa.models.UserResponse
import com.example.mtaa.utilities.Validator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // toolbar elements
    private lateinit var btnBack: ImageView

    // registration elements
    private lateinit var btnRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    companion object {
        private const val TAG: String = "RegistrationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sessionManager = SessionManager(this)

        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnBack = findViewById(R.id.btnBack)

        btnRegister.setOnClickListener {
            // validate user input
            if (!Validator.validateEmail(etEmail) ||
                !Validator.validatePassword(etPassword, etConfirmPassword)
            ) {
                return@setOnClickListener
            }
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()
            val newUser = UserToRegister(email, password)
            registerUser(newUser)
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun registerUser(newUser: UserToRegister) {
        ApiClient.getApiService(applicationContext)
            .registerUser(newUser)
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<UserResponse>, response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else if (response.code() == 400) {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            val detail = jObjError.getString("detail").toString()
                            when {
                                detail.contains("email", true) -> {
                                    etEmail.error = detail
                                    etEmail.requestFocus()
                                }
                                detail.contains("password", true) -> {
                                    etPassword.error = detail
                                    etPassword.requestFocus()
                                }
                                else -> {
                                    Toast.makeText(
                                        applicationContext,
                                        "Error: $detail", Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun handleSuccessfulResponse(response: Response<UserResponse>) {
        val registeredUser: UserResponse? = response.body()
        if (registeredUser != null) {
            Toast.makeText(
                applicationContext,
                "User registered successfully", Toast.LENGTH_LONG
            ).show()

            // save user to shared preferences
            sessionManager.saveUserId(registeredUser.id)
            sessionManager.saveUserEmail(registeredUser.email)

            // TODO login user

            // go to home activity
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun handleNotSuccessfulResponse(response: Response<UserResponse>) {
        val msg = "${response.code()} ${response.errorBody()!!.string()}"
        Log.d(TAG, "onResponse: $msg")
        Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }
}