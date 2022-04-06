package com.example.mtaa.models

import java.util.Date

data class MeetingResponse(
    val id: Int,
    val title: String,
    val duration: Int,
    val date: Date,
)
