package com.example.mtaa.models

import java.util.Date

data class MeetingRequest(
    val title: String,
    val date: Date,
    val duration: Int
)
