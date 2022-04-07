package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.utilities.Utils

class ScheduleMeetActivity : AppCompatActivity() {

    private lateinit var btnManageUsers: Button
    private lateinit var btnCreateMeeting: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView
    private lateinit var etTitle: EditText
    private lateinit var etTime: EditText
    private lateinit var etDuration: EditText

    companion object {
        private const val TAG: String = "ScheduleMeetActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_meet)

        btnManageUsers = findViewById(R.id.btnManageUsers)
        btnCreateMeeting = findViewById(R.id.btnCreateMeeting)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)
        etTitle = findViewById(R.id.etTitle)
        etTime = findViewById(R.id.etTime)
        etDuration = findViewById(R.id.etDuration)

        btnManageUsers.setOnClickListener {
            val intent = Intent(applicationContext, ManageUsersActivity::class.java)
            startActivity(intent)
        }

        btnCreateMeeting.setOnClickListener {
            if (!Utils.validateTitle(etTitle) ||
                !Utils.validateTime(etTime) ||
                !Utils.validateDuration(etDuration)
            ) {
                return@setOnClickListener
            }
            // TODO: Create meeting

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btnHome.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
    }
}