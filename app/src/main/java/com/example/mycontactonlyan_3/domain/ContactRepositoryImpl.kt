package com.example.mycontactonlyan_3.domain

import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.mepper.ContactMapper.toUIData
import com.example.mycontactonlyan_3.data.model.ContactUIData
import com.example.mycontactonlyan_3.data.model.MyShared
import com.example.mycontactonlyan_3.data.model.StatusEnum
import com.example.mycontactonlyan_3.data.model.toStatusEnum
import com.example.mycontactonlyan_3.data.sourse.local.dao.ContactDao
import com.example.mycontactonlyan_3.data.sourse.local.entity.ContactEntity
import com.example.mycontactonlyan_3.data.sourse.remote.api.ContactApi
import com.example.mycontactonlyan_3.data.sourse.remote.request.CreateContactRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.EditContactByRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.LoginRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.RegisterUserRequest
import com.example.mycontactonlyan_3.data.sourse.remote.request.VerifyUserRequest
import com.example.mycontactonlyan_3.data.sourse.remote.response.ContactResponse
import com.example.mycontactonlyan_3.data.sourse.remote.response.TokenResponse
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import com.example.mycontactonlyan_3.utils.flowResponse
import com.example.mycontactonlyan_3.utils.toResultData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
    private val gson: Gson,
    private val api: ContactApi,
    private val networkStatusValidator: NetworkStatusValidator,
) : ContactRepository {


    private fun token(): String = MyShared.getToken()

    override fun getAllContact(): Flow<ResultData<List<ContactUIData>>> = flowResponse {


        val remoteList = api.getAllContact(token())
        val list = margeDate(remoteList.body() ?: emptyList(), contactDao.getAllContactLocal())
        emit(remoteList.toResultData { list })


    }


    override fun loginContact(loginRequest: LoginRequest): Flow<ResultData<TokenResponse>> =
        flowResponse {
            val tokenResponse = api.loginContact2(loginRequest)
            emit(tokenResponse.toResultData { it }) // Return the tokenResponse itself
        }

    override fun registerContact(registerRequest: RegisterUserRequest): Flow<ResultData<Unit>> =
        flowResponse {
            emit(api.registerContact(registerRequest).toResultData {})
        }

    override fun verifyContact(verifyRequest: VerifyUserRequest): Flow<ResultData<TokenResponse>> =
        flowResponse {
            emit(api.verifyContact(verifyRequest).toResultData { it })
        }

    override fun addContact(addRequest: CreateContactRequest): Flow<ResultData<Unit>> =
        flowResponse {
            if (networkStatusValidator.hasNetwork) {
                emit(api.addContact(token(), addRequest).toResultData {})
            } else {
                contactDao.addContact(
                    ContactEntity(
                        0,
                        0,
                        addRequest.firstName,
                        addRequest.lastName,
                        addRequest.phone,
                        StatusEnum.ADD.statusCode
                    )
                )
                emit(ResultData.success(Unit))
            }
        }

    override fun editContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        status: Int
    ): Flow<ResultData<Unit>> = flowResponse {
        if (networkStatusValidator.hasNetwork) {
            emit(
                api.editContact(token(), EditContactByRequest(id, firstName, lastName, phone))
                    .toResultData { })
        } else {
            contactDao.addContact(
                ContactEntity(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    statusCode = StatusEnum.EDIT.statusCode,
                    remoteId = id
                )
            )
            emit(ResultData.success(Unit))
        }
    }

    override fun deleteContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        status: Int,
    ): Flow<ResultData<Unit>> = flowResponse {
        if (networkStatusValidator.hasNetwork) {
            emit(api.deleteContact(token(), id).toResultData { })
        } else {
            contactDao.addContact(
                ContactEntity(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    statusCode = StatusEnum.DELETE.statusCode,
                    remoteId = id
                )
            )
            emit(ResultData.success(Unit))
        }
    }

    override fun syncWithServer(): Flow<ResultData<Unit>> = flowResponse {
        val list = contactDao.getAllContactLocal()
        list.forEach {
            when (it.statusCode) {
                StatusEnum.ADD.statusCode -> {
                    emit(api.addContact(
                        token(),
                        CreateContactRequest(it.firstName, it.lastName, it.phone)
                    ).toResultData {})
                }

                StatusEnum.EDIT.statusCode -> {
                    emit(
                        api.editContact(
                            token(),
                            EditContactByRequest(it.id, it.firstName, it.lastName, it.phone)
                        ).toResultData {})
                }

                StatusEnum.DELETE.statusCode -> {
                    emit(api.deleteContact(token(), it.id).toResultData {})
                }
            }
            contactDao.deleteContact(it.id)
        }
    }

    fun margeDate(
        remoteList: List<ContactResponse>,
        localList: List<ContactEntity>
    ): List<ContactUIData> {
        val result = ArrayList<ContactUIData>()
        result.addAll(remoteList.map { it.toUIData() })

        var index = remoteList.lastOrNull()?.id ?: 0
        localList.forEach { entity ->
            when (entity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    result.add(entity.toUIData(++index))
                }

                StatusEnum.EDIT -> {
                    val findData = result.find { it.id == entity.remoteId }
                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        result[findIndex] = newData
                    }
                }

                StatusEnum.DELETE -> {
                    val findData = result.find { it.id == entity.remoteId }
                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        result[findIndex] = newData
                    }
                }

                StatusEnum.SYNC -> {}
            }
        }
        return result
    }

}












