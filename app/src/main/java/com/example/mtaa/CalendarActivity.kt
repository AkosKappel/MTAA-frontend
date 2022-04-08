package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.adapters.MeetingsAdapter
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.MeetingResponse
import com.example.mtaa.utilities.Utils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CalendarActivity : AppCompatActivity() {

    private lateinit var cvCalendar: CalendarView
    private lateinit var rvMeetings: RecyclerView
    private lateinit var tvEmpty: TextView
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
        rvMeetings = findViewById(R.id.rvMeetings)
        tvEmpty = findViewById(R.id.tvEmpty)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)

        fetchMeetings()

        cvCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedMeetings = getMeetingsOnDate(year, month, dayOfMonth)
            showMeetings(selectedMeetings)
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

    private fun fetchMeetings() {
        ApiClient.getApiService(applicationContext)
            .getMeetings()
            .enqueue(object : Callback<List<MeetingResponse>> {
                override fun onFailure(call: Call<List<MeetingResponse>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<MeetingResponse>>, response: Response<List<MeetingResponse>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun handleSuccessfulResponse(response: Response<List<MeetingResponse>>) {
        allMeetings = response.body()!!
        Log.d(TAG, "Received ${allMeetings.size} meetings")
        Calendar.getInstance().apply {
            val selectedMeetings = getMeetingsOnDate(
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH)
            )
            showMeetings(selectedMeetings)
        }
    }

    private fun handleNotSuccessfulResponse(response: Response<List<MeetingResponse>>) {
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

    private fun getMeetingsOnDate(year: Int, month: Int, day: Int): List<MeetingResponse> {
        val meetingsOnDate = allMeetings.filter { meeting ->
            val calendar = Utils.dateToCalendar(meeting.date)
            val meetingYear = calendar.get(Calendar.YEAR)
            val meetingMonth = calendar.get(Calendar.MONTH)
            val meetingDay = calendar.get(Calendar.DAY_OF_MONTH)
            meetingYear == year && meetingMonth == month && meetingDay == day
        }

        if (meetingsOnDate.isEmpty()) {
            rvMeetings.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            rvMeetings.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
        }

        return meetingsOnDate
    }

    private fun showMeetings(meetings: List<MeetingResponse>) {
        rvMeetings.layoutManager = LinearLayoutManager(applicationContext)
        rvMeetings.adapter = MeetingsAdapter(meetings)
    }
}