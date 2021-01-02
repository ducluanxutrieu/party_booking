package com.uit.party.ui.sign_in

import com.uit.party.data.Result
import com.uit.party.model.*
import com.uit.party.util.Constants
import com.uit.party.util.ServiceRetrofit
import com.uit.party.util.Storage
import com.uit.party.util.handleRequest
import javax.inject.Inject


@SignInScope
class SignInRepository @Inject constructor(
    private val networkService: ServiceRetrofit,
    private val storage: Storage
) {

    suspend fun register(registerModel: RegisterModel): Result<AccountResponse> {
        return handleRequest {
            networkService.register(registerModel)
        }
    }

    suspend fun login(model: LoginModel): Result<AccountResponse> {
        return handleRequest {
            networkService.login(model)
        }
    }

    fun saveToMemory(model: Account?, token: String? = null) {
        storage.setData(Constants.USER_INFO_KEY, model)
        if (!token.isNullOrEmpty())
            storage.setData(Constants.TOKEN_ACCESS_KEY, model?.token)
    }

    suspend fun resetPassword(username: String): Result<BaseResponse> {
        return handleRequest {
            networkService.resetPassword(username)
        }
    }
}