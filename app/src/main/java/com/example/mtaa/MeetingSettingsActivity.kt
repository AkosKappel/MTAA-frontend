package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MeetingSettingsActivity : AppCompatActivity() {

    private lateinit var btnManageUsers: Button
    private lateinit var btnDeleteMeeting: Button
    private lateinit var btnSaveChanges: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_settings)

        btnManageUsers = findViewById(R.id.btnManageUsers)
        btnDeleteMeeting = findViewById(R.id.btnDeleteMeeting)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)


        btnManageUsers.setOnClickListener {
            val intent = Intent(this, ManageUsersActivity::class.java)
            startActivity(intent)
        }

    }
}