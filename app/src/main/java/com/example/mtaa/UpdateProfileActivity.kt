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

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var btnUpdateProfile: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    companion object {
        private const val TAG: String = "UpdateProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}