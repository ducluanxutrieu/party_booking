package com.uit.party.user

import com.uit.party.data.Result
import com.uit.party.model.*
import com.uit.party.ui.sign_in.SignInRepository
import com.uit.party.util.Constants
import com.uit.party.util.Constants.Companion.REGISTERED_USER
import com.uit.party.util.Constants.Companion.TOKEN_ACCESS_KEY
import com.uit.party.util.Storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val storage: Storage,
    private val userComponentFactory: UserComponent.Factory,
    private val signInRepository: SignInRepository
) {

    private var userComponent: UserComponent? = null

    val username: String
        get() = storage.getData(REGISTERED_USER, String::class.java) ?: ""

    private val role: Int?
        get() = storage.getData(Constants.USER_INFO_KEY, Account::class.java)?.role

    val isAdmin: Boolean
        get() = role == null && (role == UserRole.Admin.ordinal || role == UserRole.Staff.ordinal)

    var userAccount: Account? = storage.getData(Constants.USER_INFO_KEY, Account::class.java)

    //    fun isUserLoggedIn() = userDataRepository != null
//    fun isUserLoggedIn() = userComponent != null

    fun checkUserLoggedIn(): Boolean{
        val token = storage.getData(TOKEN_ACCESS_KEY, String::class.java)
        if (token != null && userAccount != null) {
            userJustLoggedIn()
            return true
        }
        return false
    }

    suspend fun registerUser(model: RegisterModel): Result<AccountResponse> {
        return signInRepository.register(model)
//        userJustLoggedIn()
    }

    suspend fun loginUser(model: LoginModel): Result<AccountResponse> {
        val result = signInRepository.login(model)

        if (result is Result.Success) {
            signInRepository.saveToMemory(result.data.account, result.data.account?.token)
        }

        userJustLoggedIn()

        return result
    }

    fun updateUserInfoToShared(userAccount: Account?){
        signInRepository.saveToMemory(userAccount)
    }

    private fun userJustLoggedIn() {
        userComponent = userComponentFactory.create()
    }

    suspend fun resetPassword(username: String): Result<BaseResponse> {
        return signInRepository.resetPassword(username)
    }
}