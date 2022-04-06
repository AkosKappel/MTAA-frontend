package com.example.mtaa.models

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserResponse(
    val id: String,
    val email: String,
//    val createdAt: String
)
