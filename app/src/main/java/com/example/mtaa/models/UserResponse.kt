package com.example.mtaa.models

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserResponse(
    val id: String,
    val email: String,
    @SerializedName("created_at") val createdAt: Date
)
