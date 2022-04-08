package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.TokenData
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

        sessionManager = SessionManager(applicationContext)

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
                        handleSuccessfulResponseRegister(response, newUser)
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
                        handleNotSuccessfulResponseRegister(response)
                    }
                }
            })
    }

    private fun handleSuccessfulResponseRegister(
        response: Response<UserResponse>,
        newUser: UserToRegister
    ) {
        val registeredUser: UserResponse? = response.body()
        if (registeredUser != null) {
            Log.d(TAG, "User registered successfully")

            // save user to shared preferences
            sessionManager.saveUserId(registeredUser.id)
            sessionManager.saveUserEmail(registeredUser.email)

            loginUser(newUser.email, newUser.password)
        }
    }

    private fun handleNotSuccessfulResponseRegister(response: Response<UserResponse>) {
        val msg = "${response.code()} ${response.errorBody()!!.string()}"
        Log.d(TAG, "onResponse: $msg")
        Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }

    private fun loginUser(email: String, password: String) {
        ApiClient.getApiService(applicationContext)
            .loginUser(email, password)
            .enqueue(object : Callback<TokenData> {
                override fun onFailure(call: Call<TokenData>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<TokenData>, response: Response<TokenData>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponseLogin(response, email)
                    } else {
                        handleNotSuccessfulResponseLogin(response)
                    }
                }
            })
    }

    private fun handleSuccessfulResponseLogin(response: Response<TokenData>, email: String) {
        val loginResponse: TokenData? = response.body()
        if (loginResponse != null) {
            Log.d(TAG, "User logged in successfully")
            saveLoggedInUserData(email, loginResponse.accessToken)
            switchActivity(MainActivity::class.java, true)
        } else {
            try {
                val jObjError = JSONObject(response.errorBody()!!.string())
                val detail = jObjError.getString("detail").toString()
                if (detail.contains("password", true)) {
                    etPassword.error = detail
                    etPassword.requestFocus()
                }
            } catch (e: Exception) {
                Log.d(TAG, "onResponse: ${e.message}")
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun handleNotSuccessfulResponseLogin(response: Response<TokenData>) {
        val detail = "Invalid credentials"
        etEmail.error = detail
        etPassword.error = detail
        Log.d(TAG, "onResponse: ${response.code()} ${response.errorBody()!!.string()}")
        Toast.makeText(applicationContext, detail, Toast.LENGTH_SHORT).show()
    }

    private fun saveLoggedInUserData(email: String, token: String) {
        sessionManager.saveAuthToken(token)
        sessionManager.saveUserEmail(email)
    }

    private fun switchActivity(targetActivity: Class<*>, finishCurrent: Boolean = false) {
        val intent = Intent(applicationContext, targetActivity)
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }

}