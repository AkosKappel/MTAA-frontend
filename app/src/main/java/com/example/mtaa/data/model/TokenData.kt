package com.example.mtaa.data.model

import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)
