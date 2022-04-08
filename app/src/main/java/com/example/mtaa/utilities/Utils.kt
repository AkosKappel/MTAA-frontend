package com.example.mtaa.utilities

import android.util.Log
import android.widget.Toast
import org.json.JSONObject
import retrofit2.Response
import java.util.Calendar
import java.util.Date

object Utils {

    private val TAG = "Utils"

    fun dateToCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    fun formatDate(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return String.format(
            "%02d.%02d.%04d",
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.YEAR)
        )
    }

    fun formatTime(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return String.format(
            "%02d:%02d",
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    fun formatDateAndTime(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return String.format(
            "%02d.%02d.%04d %02d:%02d",
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }

    fun getDateFromTimeString(calendar: Calendar, time: String): Date {
        val timeArray = time.split(":")
        val hour = timeArray[0].toInt()
        val minute = timeArray[1].toInt()
        val timeZoneOffset = calendar.timeZone.rawOffset / 3600000 + 1
        calendar.set(Calendar.HOUR_OF_DAY, hour - timeZoneOffset)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun getErrorBodyDetail(json: JSONObject?): String {
        try {
            return json?.getString("detail").toString()
        } catch (e: Exception) {
            Log.d(TAG, "getErrorBodyDetail: $e")
        }
        return ""
    }

}