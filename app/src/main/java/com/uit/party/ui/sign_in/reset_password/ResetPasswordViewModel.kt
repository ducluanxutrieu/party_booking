package com.uit.party.ui.sign_in.reset_password

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.party.data.Result
import com.uit.party.user.UserManager
import com.uit.party.util.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(private val userManager: UserManager) :ViewModel(){
    var errorUsername: ObservableField<String> = ObservableField()
    val mShowLoading = ObservableBoolean(false)

    private val _resetState = MutableLiveData<Pair<Boolean, String?>>()
    val resetPassState: LiveData<Pair<Boolean, String?>>
        get() = _resetState

    fun requestResetPassword(username: String){
        mShowLoading.set(true)

        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = userManager.resetPassword(username)
                if (result is Result.Success){
                    _resetState.postValue(Pair(true, result.data.message))
                }else {
                    _resetState.postValue(Pair(true, (result as Result.Error).exception.toString()))
                }
            }catch (ex: Exception){
                _resetState.postValue(Pair(true, ex.message))
            }finally {
                mShowLoading.set(false)
            }
        }
    }
}