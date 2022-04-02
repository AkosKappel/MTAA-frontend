package com.example.mtaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var btnUpdateProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
    }
}