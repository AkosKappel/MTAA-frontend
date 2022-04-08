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

    @GET("/contacts")
    fun getContacts(): Call<List<ContactList>>

    @POST("/contacts/{contact_id}")
    fun addContact(
        @Path("contact_id") contact_id: Int
    ): Call<List<ContactList>>

    @DELETE("/contacts/{contact_id}")
    fun removeContact(
        @Path("contact_id") contact_id: Int
    ): Call<List<ContactList>>

    @GET("/calls/{call_id}/users")
    fun getUsersOfCall(
        @Path("call_id") call_id: Int
    ): Call<List<ContactList>>

    @POST("/calls/{call_id}/users/{user_id}")
    fun addUserToCall(
        @Path("call_id") call_id: Int,
        @Path("user_id") user_id: Int
    ): Call<List<ContactList>>

    @DELETE("/calls/{call_id}/users/{user_id}")
    fun removeUserFromCall(
        @Path("call_id") call_id: Int,
        @Path("user_id") user_id: Int
    ): Call<List<ContactList>>


//    @POST("/users/calls")
//    fun createCall(
//        @Body call: Call<>
//    ): Call<CallResponse>

}