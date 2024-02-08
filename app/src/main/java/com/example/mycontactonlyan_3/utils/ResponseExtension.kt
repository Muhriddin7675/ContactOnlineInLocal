package com.example.mycontactonlyan_3.utils

import com.example.mycontactonlyan_3.data.ResultData
import com.example.mycontactonlyan_3.data.sourse.remote.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import retrofit2.Response

val gson = Gson()

fun <T, R> Response<T>.toResultData(block: (T) -> R): ResultData<R> {
    return when (code()) {
        in 200 until 300 -> ResultData.success(block(body()!!))
        in 400 until 500 -> {
            val data = gson.fromJson(errorBody()!!.string(), ErrorResponse::class.java)
            ResultData.failure(data.message)
        }

        else -> ResultData.failure("Server bilan aloqa yo'q !")
    }
}

fun <T> flowResponse(block: suspend FlowCollector<ResultData<T>>.() -> Unit) = flow(block).flowOn(
    Dispatchers.IO
).catch { emit(ResultData.failure(it.message.toString())) }