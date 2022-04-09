package com.example.mtaa.models

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class Image (
    @SerializedName("image") val image: MultipartBody.Part
    )

