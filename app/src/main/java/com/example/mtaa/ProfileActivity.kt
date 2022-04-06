package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnUpdateProfile: Button
    private lateinit var btnCalendar: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var tvUserId: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserDate: TextView

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

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnUpdateProfile.setOnClickListener {
            val intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        val userId = intent.getStringExtra("id").toString()
        val userEmail = intent.getStringExtra("email").toString()
//        val userCreatedAt = intent.getStringExtra("created_at").toString()
        tvUserId.text = userId
        tvUserEmail.text = userEmail
//        tvUserDate.text = userCreatedAt
    }
}