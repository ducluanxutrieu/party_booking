package com.uit.party.ui.profile.edit_profile

import android.app.DatePickerDialog
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.party.R
import com.uit.party.data.Result
import com.uit.party.user.UserDataRepository
import com.uit.party.user.UserManager
import com.uit.party.util.*
import com.uit.party.util.TimeFormatUtil.formatDateToClient
import com.uit.party.util.TimeFormatUtil.formatDateToServer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(private val userRepository: UserDataRepository, userManager: UserManager) : ViewModel() {
    val account = userManager.userAccount
    private var fullNameValid = false
    private var phoneNumberValid = false
    private var emailValid = false

    var errorFullName = ObservableField("")
    var errorPhoneNumber = ObservableField("")
    var errorEmailText = ObservableField("")

    val mFullName = ObservableField("")
    val mPhoneNumber = ObservableField("")
    val mEmail = ObservableField("")
    val mBirthday = ObservableField("")
    var mSex: String = ""

    var btnUpdateEnabled: ObservableBoolean = ObservableBoolean()


    private var calBirthdayPicker = Calendar.getInstance()
    private val calDateNow = Calendar.getInstance()

    private val _messageCallback = MutableLiveData<Pair<Boolean, String?>>()
    val messageCallback: LiveData<Pair<Boolean, String?>>
        get() = _messageCallback

    private val birthDaySetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calBirthdayPicker.set(Calendar.YEAR, year)
            calBirthdayPicker.set(Calendar.MONTH, monthOfYear)
            calBirthdayPicker.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (calBirthdayPicker >= calDateNow) {
                UiUtil.showToast(UiUtil.getString(R.string.birthday_greater_than_now_day))
            } else {
                updateBirthdayInView()
            }
        }

    init {
//        val timeStart = sf.format(calBirthdayPicker.time)
        if (account?.birthday.isNullOrEmpty()) {
            mBirthday.set(account?.birthday)
        }
        if (!account?.email.isNullOrEmpty()) {
            mEmail.set(account?.email)
            emailValid = true
            checkEnableButtonUpdate()
        }
        mPhoneNumber.set(account?.phone.toString())
        phoneNumberValid = true
        checkEnableButtonUpdate()
        if (!account?.fullName.isNullOrEmpty()) {
            mFullName.set(account?.fullName)
            fullNameValid = true
            checkEnableButtonUpdate()
        }

        if (!account?.birthday.isNullOrEmpty()) {
            mBirthday.set(account?.birthday.formatDateToClient())
        }
    }

    private fun updateBirthdayInView() {
        val formatDateUI = "dd-MM-yyyy"
        val sf = SimpleDateFormat(formatDateUI, Locale.US)
        val timeStart = sf.format(calBirthdayPicker.time)
        this.mBirthday.set(timeStart)
    }

    private fun checkEnableButtonUpdate() {
        if (fullNameValid && phoneNumberValid && emailValid) {
            btnUpdateEnabled.set(true)
        } else btnUpdateEnabled.set(false)
    }

    fun checkEmailValid(text: CharSequence?) {
        val message = text.emailErrorMes()
        errorEmailText.set(message)
        emailValid = message.isEmpty()
        checkEnableButtonUpdate()
    }

    fun checkFullNameValid(text: CharSequence?) {
        val message = text.requireFieldErrorMes()
        errorFullName.set(message)
        fullNameValid = message.isEmpty()
        checkEnableButtonUpdate()
    }

    fun checkPhoneNumberValid(text: CharSequence?) {
        val message = text.phoneErrorMes()
        errorPhoneNumber.set(message)
        phoneNumberValid = message.isEmpty()
        checkEnableButtonUpdate()
    }

    fun onBirthdayClicked(view: View) {
        DatePickerDialog(
            view.context,
            birthDaySetListener,
            calBirthdayPicker.get(Calendar.YEAR),
            calBirthdayPicker.get(Calendar.MONTH),
            calBirthdayPicker.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun onUpdateClicked() {
        val requestModel = RequestUpdateProfile(
            mEmail.get(),
            mFullName.get(),
            mPhoneNumber.get(),
            mBirthday.get().formatDateToServer(),
            mSex
        )

        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = userRepository.updateUser(requestModel)
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
            }
        }
    }
}