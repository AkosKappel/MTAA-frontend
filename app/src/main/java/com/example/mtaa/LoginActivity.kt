package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.storage.SessionManager
import com.example.mtaa.models.TokenData
import com.example.mtaa.utilities.Settings
import com.example.mtaa.utilities.Utils
import com.example.mtaa.utilities.Validator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // login form elements
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

        // init env variables
        sessionManager = SessionManager(applicationContext)
        Settings.initEnv(assets)

        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin.setOnClickListener {
            if (!Validator.validateEmail(etEmail) || !Validator.validatePassword(etPassword)) {
                return@setOnClickListener
            }
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()
            loginUser(email, password)
        }

        btnRegister.setOnClickListener { switchActivity(RegistrationActivity::class.java) }
    }

    // login logic
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
                        handleSuccessfulResponse(response, email)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
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

    override fun onStart() {
        super.onStart()

        // check if user is already logged in
        if (sessionManager.isUserLoggedIn()) {
            switchActivity(MainActivity::class.java, true)
        }
    }

    private fun handleSuccessfulResponse(response: Response<TokenData>, email: String) {
        val loginResponse: TokenData? = response.body()
        if (loginResponse != null) {
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
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun handleNotSuccessfulResponse(response: Response<TokenData>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        val msg = "Invalid credentials"
        Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }
}