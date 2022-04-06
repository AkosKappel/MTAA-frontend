package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mtaa.api.ApiClient
import com.example.mtaa.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnUpdateProfile: Button
    private lateinit var btnCalendar: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var tvUserId: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserDate: TextView

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

        getUserData()

        btnHome.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnUpdateProfile.setOnClickListener {
            val intent = Intent(applicationContext, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(applicationContext, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserData() {
        ApiClient.getApiService(applicationContext)
            .getUser()
            .enqueue(object : Callback<UserResponse> {
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val user = response.body()
                    val userId = user?.id ?: "ID not found"
                    val userEmail = user?.email ?: "Email not found"
//                    val userRegistrationDate = user?.createdAt ?: "Registration date not found"

                    // set fields
                    tvUserId.text = userId
                    tvUserEmail.text = userEmail
//                    tvUserDate.text = userRegistrationDate
                }
            })
    }
}