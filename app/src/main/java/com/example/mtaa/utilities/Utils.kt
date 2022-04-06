package com.example.mtaa.utilities

import java.util.Calendar
import java.util.Date

object Utils {

    var env: Map<String, String>? = null

    fun dateToCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

}