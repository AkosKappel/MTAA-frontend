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

    @PUT("calls/{id}")
    fun updateMeeting(
        @Path("id") id: Int,
        @Body meeting: MeetingRequest
    ): Call<MeetingResponse>

    @GET("/contacts")
    fun getContacts(): Call<List<ContactList>>

//    @POST("/users/calls")
//    fun createCall(
//        @Body call: Call<>
//    ): Call<CallResponse>

}