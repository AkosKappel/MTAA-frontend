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
import com.example.mtaa.data.SessionManager
import com.example.mtaa.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnStartCall: Button
    private lateinit var btnScheduleMeeting: Button
    private lateinit var btnCalendar: Button
    private lateinit var btnManageContacts: Button
    private lateinit var btnLogOut: Button
    private lateinit var btnProfile: ImageView

    companion object {
        private const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        // get buttons from view
        btnStartCall = findViewById(R.id.btnStartCall)
        btnScheduleMeeting = findViewById(R.id.btnScheduleMeeting)
        btnCalendar = findViewById(R.id.btnCalendar)
        btnManageContacts = findViewById(R.id.btnManageContacts)
        btnLogOut = findViewById(R.id.btnLogOut)
        btnProfile = findViewById(R.id.btnProfile)

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        // set onClickListeners
        btnStartCall.setOnClickListener {
            // start call activity
            val intent = Intent(this, StartCallActivity::class.java)
            startActivity(intent)
        }
        btnScheduleMeeting.setOnClickListener {
            // start schedule meeting activity
            val intent = Intent(this, ScheduleMeetActivity::class.java)
            startActivity(intent)
        }
        btnCalendar.setOnClickListener {
            // start calendar activity
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        btnManageContacts.setOnClickListener {
            // start manage contacts activity
            val intent = Intent(this, ManageContactsActivity::class.java)
            startActivity(intent)
        }
        btnLogOut.setOnClickListener {
            // logout current user and go to login activity
            sessionManager.deleteAuthToken()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}