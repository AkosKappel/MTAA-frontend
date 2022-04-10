package com.example.mtaa.api

import com.example.mtaa.models.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // ----------------------
    // |   AUTHORIZATION    |
    // ----------------------

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

    // ----------------------
    // |       USERS        |
    // ----------------------

    @GET("/users")
    fun getUser(): Call<UserResponse>

    @PUT("/users")
    fun updateUser(
        @Body user: UserToRegister
    ): Call<UserResponse>

    @DELETE("/users")
    fun deleteUser(): Call<Void>

    // ----------------------
    // |      MEETINGS      |
    // ----------------------

    @GET("/users/calls")
    fun getMeetings(): Call<List<MeetingResponse>>

    @POST("/users/calls")
    fun createMeeting(
        @Body meeting: MeetingRequest
    ): Call<MeetingResponse>

    @PUT("calls/{call_id}")
    fun updateMeeting(
        @Path("call_id") id: Int,
        @Body meeting: MeetingRequest
    ): Call<MeetingResponse>

    @DELETE("calls/{call_id}")
    fun deleteMeeting(
        @Path("call_id") id: Int
    ): Call<Void>

    // ----------------------
    // |  USERS & MEETINGS  |
    // ----------------------

    @GET("/calls/{call_id}/users")
    fun getUsersInMeeting(
        @Path("call_id") call_id: Int
    ): Call<List<Contact>>

    @POST("/calls/{call_id}/users/{user_id}")
    fun addUserToMeeting(
        @Path("call_id") call_id: Int,
        @Path("user_id") user_id: Int
    ): Call<List<Contact>>

    @DELETE("/calls/{call_id}/users/{user_id}")
    fun removeUserFromMeeting(
        @Path("call_id") call_id: Int,
        @Path("user_id") user_id: Int
    ): Call<List<Contact>>

    // ----------------------
    // |      CONTACTS      |
    // ----------------------

    @GET("/contacts")
    fun getContacts(): Call<List<Contact>>

    @POST("/contacts/{contact_id}")
    fun addContact(
        @Path("contact_id") contact_id: Int
    ): Call<List<Contact>>

    @DELETE("/contacts/{contact_id}")
    fun removeContact(
        @Path("contact_id") contact_id: Int
    ): Call<List<Contact>>

    // ----------------------
    // |       FILES        |
    // ----------------------

    @Multipart
    @PUT("/file/upload")
    fun uploadIMG(
        @Part image: MultipartBody.Part
    ): Call<Void>

    @GET("/file/download")
    fun downloadIMG(
    ): Call<ResponseBody>

}