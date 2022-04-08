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
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ScheduleMeetActivity : AppCompatActivity() {

    // toolbar elements
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    // meeting elements
    private lateinit var cvCalendar: CalendarView
    private lateinit var etTitle: EditText
    private lateinit var etTime: EditText
    private lateinit var etDuration: EditText
    private lateinit var btnCreateMeeting: Button

    companion object {
        private const val TAG: String = "ScheduleMeetActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_meet)

        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)
        cvCalendar = findViewById(R.id.cvCalendar)
        etTitle = findViewById(R.id.etTitle)
        etTime = findViewById(R.id.etTime)
        etDuration = findViewById(R.id.etDuration)
        btnCreateMeeting = findViewById(R.id.btnCreateMeeting)

        val dateCalendar = Calendar.getInstance()
        cvCalendar.setOnDateChangeListener { _, year, month, day ->
            dateCalendar.set(year, month, day)
        }

        btnCreateMeeting.setOnClickListener {
            if (!Validator.validateTitle(etTitle) ||
                !Validator.validateTime(etTime) ||
                !Validator.validateDuration(etDuration)
            ) {
                return@setOnClickListener
            }
            val title = etTitle.text.toString().trim()
            val time = etTime.text.toString().trim()
            val date = Utils.getDateFromTimeString(dateCalendar, time)
            val duration = etDuration.text.toString().trim().toInt()

            val newMeeting = MeetingRequest(title, date, duration)
            createMeeting(newMeeting)
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

    private fun createMeeting(meeting: MeetingRequest) {
        ApiClient.getApiService(applicationContext)
            .createMeeting(meeting)
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

    private fun handleSuccessfulResponse(response: Response<MeetingResponse>) {
        val createdMeeting: MeetingResponse? = response.body()
        if (createdMeeting != null) {
            val msg = "Meeting created successfully"
            Log.d(TAG, msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

            val intent = Intent(applicationContext, ManageUsersActivity::class.java)
            intent.putExtra("meeting", createdMeeting)
            startActivity(intent)
            finish()
        } else {
            val msg = "Meeting update failed"
            Log.d(TAG, msg)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleNotSuccessfulResponse(response: Response<MeetingResponse>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        Toast.makeText(applicationContext, "Error: $detail", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }
}