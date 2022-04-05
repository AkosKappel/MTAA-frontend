package com.example.mtaa.api

import com.example.mtaa.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("/register")
    fun registerUser(
        @Body user: User
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(
        @Field("username") email: String,
        @Field("password") password: String
    ): Call<TokenData>

//    // TODO: ukazka ako funguje id v url
//    @POST("/users/{id}")
//    fun getUser(
//        @Body user: User,
//        @Path("id") id: Int
//    ): Call<UserResponse>

    @GET("/users/calls")
    fun getCalls(): Call<CallResponse>

}