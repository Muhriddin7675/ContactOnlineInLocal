package com.example.mycontactonlyan_3.data.sourse.remote.api

import com.example.mycontactonlyan_3.data.sourse.remote.request.CreateContactRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.EditContactByRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.LoginRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.RegisterUserRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.VerifyUserRequest
import com.example.mycontactonlyan_3.data.sourse.remote.response.ContactResponse
import com.example.mycontactonlyan_3.data.sourse.remote.response.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ContactApi {

    @POST("api/v1/login")
    suspend fun loginContact2(@Body data: LoginRequest): Response<TokenResponse>

    @POST("api/v1/register")
    suspend fun registerContact(@Body data: RegisterUserRequest): Response<Unit>

    @POST("api/v1/register/verify")
    suspend fun verifyContact(@Body data: VerifyUserRequest): Response<TokenResponse>

    @GET("api/v1/contact")
    suspend fun getAllContact(@Header("token") token: String): Response<List<ContactResponse>>

    @POST("api/v1/contact")
    suspend fun addContact(
        @Header("token") token: String,
        @Body data: CreateContactRequest
    ): Response<ContactResponse>

    @PUT("api/v1/contact")
    suspend fun editContact(
        @Header("token") token: String,
        @Body data: EditContactByRequest
    ): Response<ContactResponse>

    @DELETE("api/v1/contact")
    suspend fun deleteContact(@Header("token") token: String, @Query("id") id: Int): Response<Unit>

}