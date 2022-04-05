package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.data.SessionManager
import com.example.mtaa.data.model.TokenData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    companion object {
        private const val TAG: String = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
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
            loginUser(email, password)
        }

        btnRegister.setOnClickListener { switchActivity(RegistrationActivity::class.java, true) }
    }

    // login logic
    private fun loginUser(email: String, password: String) {
        ApiClient.getApiService(applicationContext)
            .loginUser(email, password)
            .enqueue(object : Callback<TokenData> {
                override fun onFailure(call: Call<TokenData>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
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
                            switchActivity(MainActivity::class.java, true)
                        } else if (response.code() == 401) {
                            try {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                val detail = jObjError.getString("detail").toString()
                                if (detail.contains("password", true)) {
                                    etPassword.error = detail
                                    etPassword.requestFocus()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        } else {
                            Log.d(
                                TAG,
                                "onResponse: ${response.code()} ${response.errorBody()!!.string()}"
                            )
                            Toast.makeText(
                                applicationContext,
                                "Error: ${response.code()} ${response.errorBody()!!.string()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }

    private fun switchActivity(targetActivity: Class<*>, finishCurrent: Boolean = false) {
        val intent = Intent(applicationContext, targetActivity)
        startActivity(intent)
        if (finishCurrent) {
            finish()
        }
    }

}