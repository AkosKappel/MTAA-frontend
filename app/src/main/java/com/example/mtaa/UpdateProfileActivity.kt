package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.mtaa.storage.SessionManager
import com.example.mtaa.utilities.Utils

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // toolbar elements
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    // window elements
    private lateinit var tvUploadImage: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnUpdateProfile: Button

    companion object {
        private const val TAG: String = "UpdateProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        sessionManager = SessionManager(this)
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        btnProfile = findViewById(R.id.btnProfile)
        tvUploadImage = findViewById(R.id.tvUploadImage)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        val mSpannableString = SpannableString(tvUploadImage.text)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        tvUploadImage.text = mSpannableString

        // fill user data
        etEmail.setText(sessionManager.fetchUserEmail())

        btnUpdateProfile.setOnClickListener {
            if (!Utils.validateEmail(etEmail) || !Utils.validatePassword(etPassword)) {
                return@setOnClickListener
            }
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            // TODO: update profile
        }

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
    }
}