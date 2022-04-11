package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.storage.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    // toolbar elements
    private lateinit var btnProfile: ImageView

    // main buttons
    private lateinit var btnStartCall: Button
    private lateinit var btnScheduleMeeting: Button
    private lateinit var btnCalendar: Button
    private lateinit var btnManageContacts: Button
    private lateinit var btnLogOut: Button

    companion object {
        private const val TAG: String = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(applicationContext)

        // get buttons from view
        btnStartCall = findViewById(R.id.btnStartCall)
        btnScheduleMeeting = findViewById(R.id.btnScheduleMeeting)
        btnCalendar = findViewById(R.id.btnCalendar)
        btnManageContacts = findViewById(R.id.btnManageContacts)
        btnLogOut = findViewById(R.id.btnLogOut)
        btnProfile = findViewById(R.id.btnProfile)

        // set onClickListeners
        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnStartCall.setOnClickListener {
//            val intent = Intent(applicationContext, StartCallActivity::class.java)
            val intent = Intent(applicationContext, CallActivity::class.java)
            startActivity(intent)
        }

        btnScheduleMeeting.setOnClickListener {
            val intent = Intent(applicationContext, ScheduleMeetActivity::class.java)
            startActivity(intent)
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(applicationContext, CalendarActivity::class.java)
            startActivity(intent)
        }

        btnManageContacts.setOnClickListener {
            val intent = Intent(applicationContext, ManageContactsActivity::class.java)
            startActivity(intent)
        }

        btnLogOut.setOnClickListener {
            deleteLoggedInUserData()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun deleteLoggedInUserData() {
        sessionManager.deleteAuthToken()
        sessionManager.deleteUserEmail()
    }
}