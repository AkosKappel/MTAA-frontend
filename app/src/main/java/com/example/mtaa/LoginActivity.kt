package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mtaa.api.ApiClient
import com.example.mtaa.data.SessionManager
import com.example.mtaa.data.model.Post
import com.example.mtaa.data.model.TokenData
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
//            ApiClient.getApiService(this@LoginActivity).getPost()
//                .enqueue(object : retrofit2.Callback<Post> {
//                    override fun onFailure(call: Call<Post>, t: Throwable) {
//                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
//                        if (response.isSuccessful) {
//                            val title: String = response.body()?.title.toString()
//                            Toast.makeText(applicationContext, title, Toast.LENGTH_LONG).show()
//                        } else {
//                            Toast.makeText(
//                                applicationContext,
//                                "${response.code()} ${response.message()}",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                })

            val email: String = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            val password: String = etPassword.text.toString().trim()
            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // login logic
            ApiClient.getApiService(this@LoginActivity).loginUser(email, password)
                .enqueue(object : retrofit2.Callback<TokenData> {
                    override fun onFailure(call: Call<TokenData>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<TokenData>,
                        response: Response<TokenData>
                    ) {
                        if (response.isSuccessful) {
                            val loginResponse: TokenData? = response.body()
                            if (response.isSuccessful && loginResponse != null) {
                                sessionManager.saveAuthToken(loginResponse.accessToken)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "${response.code()} ${response.message()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }
}