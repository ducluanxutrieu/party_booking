package com.uit.party.user

import com.uit.party.data.Result
import com.uit.party.data.PartyBookingDatabase
import com.uit.party.model.AccountResponse
import com.uit.party.model.BaseResponse
import com.uit.party.ui.profile.edit_profile.RequestUpdateProfile
import com.uit.party.util.ServiceRetrofit
import com.uit.party.util.SharedPrefs
import com.uit.party.util.handleRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserDataRepository @Inject constructor(
    private val userManager: UserManager,
    private val networkService: ServiceRetrofit,
    private val sharedPrefs: SharedPrefs,
    private val database: PartyBookingDatabase
) {


    suspend fun logout(): Result<BaseResponse> {
        val result = handleRequest {
            networkService.logout(sharedPrefs.token)
        }
        if (result is Result.Success){
            clearData()
        }
        return result
    }

    suspend fun changeAvatar(path: String): Result<BaseResponse> {
        val file = File(path)
        val parseType = "multipart/form-data"

        val part: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody(parseType.toMediaTypeOrNull())
        )

        //Create request body with text description and text media type
        val description = "image-type".toRequestBody("text/plain".toMediaTypeOrNull())
        val result = handleRequest {
            networkService.updateAvatar(sharedPrefs.token, part, description)
        }
        if (result is Result.Success){
            userManager.userAccount?.avatar = result.data.message
            userManager.updateUserInfoToShared(userManager.userAccount)
        }

        return result
    }

    private fun clearData() {
        sharedPrefs.clear()
        database.clearAllTables()
    }

    suspend fun changePassword(password: String, newPassword: String): Result<BaseResponse> {
        return handleRequest {
            networkService.changePassword(sharedPrefs.token, password, newPassword)
        }
    }

    suspend fun verifyPassword(currentPassword: String, newPassword: String): Result<BaseResponse> {
        return handleRequest {
            networkService.verifyPassword(currentPassword, newPassword)
        }
    }

    suspend fun updateUser(requestModel: RequestUpdateProfile): Result<AccountResponse> {
        val result = handleRequest { networkService.updateUser(sharedPrefs.token, requestModel) }

        if (result is Result.Success){
            userManager.userAccount = result.data.account
            userManager.updateUserInfoToShared(result.data.account)
        }

        return result
    }
}
