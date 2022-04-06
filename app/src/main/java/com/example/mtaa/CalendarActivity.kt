package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.MeetingResponse
import com.example.mtaa.utilities.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var cvCalendar: CalendarView
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    private lateinit var allMeetings: List<MeetingResponse>

    companion object {
        private const val TAG: String = "CalendarActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        cvCalendar = findViewById(R.id.cvCalendar)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)

        getMeetings()

        cvCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            getMeetingsOnDate(year, month, dayOfMonth)
            // TODO: add meetings on selected date into recycler view
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

    private fun getMeetings() {
        ApiClient.getApiService(applicationContext)
            .getMeetings()
            .enqueue(object : Callback<List<MeetingResponse>> {
                override fun onFailure(call: Call<List<MeetingResponse>>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<MeetingResponse>>,
                    response: Response<List<MeetingResponse>>
                ) {
                    if (response.isSuccessful) {
                        allMeetings = response.body()!!
                        Log.d(TAG, "Received ${allMeetings.size} meetings")
                        // TODO: add today's meetings into recycler view
                    } else {
                        Log.d(TAG, response.errorBody().toString())
                        Toast.makeText(
                            applicationContext,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getMeetingsOnDate(year: Int, month: Int, day: Int): List<MeetingResponse> {
        val meetingsOnDate = allMeetings.filter { meeting ->
            val calendar = Utils.dateToCalendar(meeting.date)
            val meetingYear = calendar.get(Calendar.YEAR)
            val meetingMonth = calendar.get(Calendar.MONTH)
            val meetingDay = calendar.get(Calendar.DAY_OF_MONTH)
            meetingYear == year && meetingMonth == month && meetingDay == day
        }
        return meetingsOnDate
    }

}