package com.example.mtaa.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class MeetingResponse(
    val id: Int,
    val title: String,
    @SerializedName("owner_id") val ownerId: Int,
    val date: Date,
    val duration: Int,
//    TODO: val users: List<T>
) : Serializable
