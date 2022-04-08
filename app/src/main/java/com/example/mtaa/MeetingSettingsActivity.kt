package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.MeetingRequest
import com.example.mtaa.models.MeetingResponse
import com.example.mtaa.utilities.Utils
import com.example.mtaa.utilities.Validator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class MeetingSettingsActivity : AppCompatActivity() {

    // toolbar elements
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    // meeting settings elements
    private lateinit var cvCalendar: CalendarView
    private lateinit var etTitle: EditText
    private lateinit var etTime: EditText
    private lateinit var etDuration: EditText
    private lateinit var btnManageUsers: Button
    private lateinit var btnDeleteMeeting: Button
    private lateinit var btnSaveChanges: Button

    private lateinit var selectedMeeting: MeetingResponse

    companion object {
        private const val TAG: String = "MeetingSettingsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_settings)

        // get elements from layout
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        btnProfile = findViewById(R.id.btnProfile)
        cvCalendar = findViewById(R.id.cvCalendar)
        etTitle = findViewById(R.id.etTitle)
        etTime = findViewById(R.id.etTime)
        etDuration = findViewById(R.id.etDuration)
        btnManageUsers = findViewById(R.id.btnManageUsers)
        btnDeleteMeeting = findViewById(R.id.btnDeleteMeeting)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)

        selectedMeeting = intent.getSerializableExtra("meeting") as MeetingResponse

        // fill in meeting settings
        cvCalendar.date = selectedMeeting.date.time
        etTitle.setText(selectedMeeting.title)
        etTime.setText(Utils.formatTime(selectedMeeting.date))
        etDuration.setText(selectedMeeting.duration.toString())

        // set on click listeners
        btnManageUsers.setOnClickListener {
            val intent = Intent(applicationContext, ManageUsersActivity::class.java)
            intent.putExtra("meeting", selectedMeeting)
            startActivity(intent)
        }

        btnDeleteMeeting.setOnClickListener {
            // TODO: delete meeting
        }

        btnSaveChanges.setOnClickListener {
            if (!Validator.validateTitle(etTitle) ||
                !Validator.validateTime(etTime) ||
                !Validator.validateDuration(etDuration)
            ) {
                return@setOnClickListener
            }
            val title = etTitle.text.toString().trim()
            val date =
                Utils.dateFromDateAndTimeString(
                    Date(cvCalendar.date),
                    etTime.text.toString().trim()
                )
            val duration = etDuration.text.toString().trim().toInt()

            val updatedMeeting = MeetingRequest(title, date, duration)
            updateMeeting(updatedMeeting)
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

    private fun updateMeeting(meeting: MeetingRequest) {
        ApiClient.getApiService(applicationContext)
            .updateMeeting(selectedMeeting.id, meeting)
            .enqueue(object : Callback<MeetingResponse> {
                override fun onFailure(call: Call<MeetingResponse>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<MeetingResponse>,
                    response: Response<MeetingResponse>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun deleteMeeting() {}


    private fun handleSuccessfulResponse(response: Response<MeetingResponse>) {
        val updatedMeeting: MeetingResponse? = response.body()
        if (updatedMeeting != null) {
            Toast.makeText(
                applicationContext,
                "Meeting updated successfully",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(applicationContext, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                applicationContext,
                "Meeting update failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleNotSuccessfulResponse(response: Response<MeetingResponse>) {
        val msg = "${response.code()} ${response.errorBody()!!.string()}"
        Log.d(TAG, "onResponse: $msg")
        Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }
}