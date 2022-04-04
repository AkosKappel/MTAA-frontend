package com.example.mtaa.api

import com.example.mtaa.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("/register")
    fun registerUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(
        @Query("username") email: String,
        @Query("password") password: String
    ): Call<TokenData>

    @GET("/users/calls")
    fun getCalls(): Call<CallResponse>

    // INFO: toto je iba pre testovanie
//    @GET("/posts/1")
//    fun getPost(): Call<Post>

}