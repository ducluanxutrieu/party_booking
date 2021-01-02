package com.uit.party.ui.profile.change_password

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.party.R
import com.uit.party.data.Result
import com.uit.party.user.UserDataRepository
import com.uit.party.util.Constants
import com.uit.party.util.UiUtil
import com.uit.party.util.confirmPasswordErrorMes
import com.uit.party.util.passwordErrorMes
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(private val userRepository: UserDataRepository) :
    ViewModel() {
    val sendButtonEnabled = ObservableBoolean(false)

    var errorCurrentPassword = ObservableField("")
    var errorNewPassword = ObservableField("")
    var errorConfirmPassword = ObservableField("")
    val mTitleActionPassword = ObservableField(UiUtil.getString(R.string.change_password))
    val mHintCodeOrPassword = ObservableField(UiUtil.getString(R.string.action_current_password))

    private var currentPasswordValid = false
    private var newPasswordValid = false
    private var confirmPasswordValid = false

    private var currentPasswordText = ""
    private var newPasswordText = ""

    val mShowLoading = ObservableBoolean(false)
    var mOrderCode = "CHANGE"

    private val _messageCallback = MutableLiveData<Pair<Boolean, String?>>()
    val messageCallback: LiveData<Pair<Boolean, String?>>
        get() = _messageCallback

    fun init(orderCode: String) {
        mOrderCode = orderCode
        if (orderCode == "CHANGE") {
            mTitleActionPassword.set(UiUtil.getString(R.string.change_password))
            mHintCodeOrPassword.set(UiUtil.getString(R.string.action_current_password))
        } else {
            mTitleActionPassword.set(UiUtil.getString(R.string.reset_password))
            mHintCodeOrPassword.set(UiUtil.getString(R.string.code_from_your_email))
        }
    }

    fun btnSendClicked() {
        mShowLoading.set(true)
        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = if (mOrderCode == "CHANGE") userRepository.changePassword(
                    currentPasswordText,
                    newPasswordText
                )
                else userRepository.verifyPassword(currentPasswordText, newPasswordText)
                if (result is Result.Success) {
                    _messageCallback.postValue(Pair(true, result.data.message))
                } else
                    _messageCallback.postValue(
                        Pair(
                            false,
                            (result as Result.Error).exception.message
                        )
                    )
            } catch (ex: Exception) {
                _messageCallback.postValue(Pair(false, ex.message))
            } finally {
                mShowLoading.set(false)
            }
        }
    }

    fun checkCurrentPasswordValid(text: CharSequence?) {
        currentPasswordText = text.toString()
        val message = text.passwordErrorMes()
        errorCurrentPassword.set(message)
        currentPasswordValid = message.isEmpty()
        checkShowSendButton()
    }

    fun checkNewPasswordValid(text: CharSequence?) {
        newPasswordText = text.toString()
        val message = text.passwordErrorMes()
        errorNewPassword.set(message)
        newPasswordValid = message.isEmpty()
        checkShowSendButton()
    }

    fun checkConfirmPasswordValid(text: CharSequence?) {
        val message = text.confirmPasswordErrorMes(newPasswordText)
        errorConfirmPassword.set(message)
        confirmPasswordValid = message.isEmpty()
        checkShowSendButton()
    }

    private fun checkShowSendButton() {
        if (currentPasswordValid && newPasswordValid) {
            sendButtonEnabled.set(true)
        } else sendButtonEnabled.set(false)
    }
}