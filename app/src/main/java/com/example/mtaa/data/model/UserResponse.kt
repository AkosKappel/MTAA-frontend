package com.example.mtaa.data.model

import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.util.*

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserResponse(
    val id: String,
    val email: String,
//    val created_at: String
)
