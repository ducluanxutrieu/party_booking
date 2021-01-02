package com.uit.party.util

import com.google.gson.Gson
import com.uit.party.data.Result
import com.uit.party.model.BaseResponse
import retrofit2.HttpException
import java.io.IOException


suspend fun <T : Any> handleRequest(requestFunc: suspend () -> T): Result<T> {
    return try {
        Result.Success(requestFunc.invoke())
    } catch (httpException: HttpException) {
        val errorMessage = getErrorMessageFromGenericResponse(httpException)
        if (errorMessage.isNullOrBlank()) {
            Result.Error(httpException)
        } else {
            Result.Error(java.lang.Exception(errorMessage))
        }
    }
}

private fun getErrorMessageFromGenericResponse(httpException: HttpException): String? {
    var errorMessage: String? = null
    try {
        val body = httpException.response()?.errorBody()
        val adapter = Gson().getAdapter(BaseResponse::class.java)
        val errorParser = adapter.fromJson(body?.string())
        errorMessage = errorParser.message
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        return errorMessage
    }
}