package com.example.mtaa.models

import java.util.Date

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserResponse(
    val id: String,
    val email: String,
    val created_at: Date
)
