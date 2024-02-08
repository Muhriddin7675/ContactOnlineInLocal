package com.example.mycontactonlyan_3.domain

import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.sourse.remote.request.CreateContactRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.LoginRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.RegisterUserRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.VerifyUserRequest
import com.example.mycontactonlyan_3.data.sourse.remote.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContact():Flow<ResultData<List<ContactUIData>>>
    fun addContact(addRequest:CreateContactRequest):Flow<ResultData<Unit>>
    fun editContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        status: Int,
    ):Flow<ResultData<Unit>>
    fun loginContact(data: LoginRequest): Flow<ResultData<TokenResponse>>
    fun registerContact(registerRequest: RegisterUserRequest): Flow<ResultData<Unit>>
    fun verifyContact(verifyRequest: VerifyUserRequest): Flow<ResultData<TokenResponse>>
    fun syncWithServer() : Flow<ResultData<Unit>>
    fun deleteContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        status: Int
    ): Flow<ResultData<Unit>>
}