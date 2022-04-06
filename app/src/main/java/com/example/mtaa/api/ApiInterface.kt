package com.example.mtaa.api

import com.example.mtaa.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("/register")
    fun registerUser(
        @Body user: UserToRegister
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

    @GET("/users")
    fun getUser(): Call<UserResponse>

    @PUT("/users")
    fun updateUser(
        @Body user: UserToRegister
    ): Call<UserResponse>

    @DELETE("/users")
    fun deleteUser(): Call<Response<Void>>

    @GET("/users/calls")
    fun getMeetings(): Call<List<MeetingResponse>>

//    @POST("/users/calls")
//    fun createCall(
//        @Body call: Call<>
//    ): Call<CallResponse>

}