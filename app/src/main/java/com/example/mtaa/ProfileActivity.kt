package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.UserResponse
import com.example.mtaa.utilities.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    // toolbar elements
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView

    // window elements
    private lateinit var tvUserId: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserDate: TextView
    private lateinit var btnUpdateProfile: Button
    private lateinit var btnCalendar: Button

    companion object {
        private const val TAG: String = "ProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
        btnCalendar = findViewById(R.id.btnCalendar)
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        tvUserId = findViewById(R.id.tvUserIDText)
        tvUserEmail = findViewById(R.id.tvEmailText)
        tvUserDate = findViewById(R.id.tvRegistrationDateText)

        fetchUser()

        btnHome.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btnUpdateProfile.setOnClickListener {
            val intent = Intent(applicationContext, UpdateProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(applicationContext, CalendarActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun fetchUser() {
        ApiClient.getApiService(applicationContext)
            .getUser()
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
        val user = response.body()
        val userId = user?.id ?: "ID not found"
        val userEmail = user?.email ?: "Email not found"
        val userRegistrationDate =
            user?.createdAt?.let { Utils.formatDate(it) } ?: "Registration date not found"

        // set fields
        tvUserId.text = userId
        tvUserEmail.text = userEmail
        tvUserDate.text = userRegistrationDate
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