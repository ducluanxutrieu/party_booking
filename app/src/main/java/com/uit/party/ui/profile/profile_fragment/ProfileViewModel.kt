package com.uit.party.ui.profile.profile_fragment

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.uit.party.R
import com.uit.party.data.Result
import com.uit.party.model.UserGender
import com.uit.party.user.UserDataRepository
import com.uit.party.user.UserManager
import com.uit.party.util.Constants
import com.uit.party.util.TimeFormatUtil.formatDateToClient
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor (userManager: UserManager, private val userRepository: UserDataRepository) : ViewModel(){
    val mName = ObservableField("")
    val mUsername = ObservableField("")
    val mEmail = ObservableField("")
    val mMobile = ObservableField("")
    val mSex = ObservableField("")
    val mBirthDay = ObservableField("")
    val mAvatar = ObservableField("")

    private var mAccount = userManager.userAccount

    init {
        mName.set(mAccount?.fullName)
        mEmail.set(mAccount?.email)
        mSex.set(UserGender.values()[mAccount?.gender ?: 0].name)
        mBirthDay.set(mAccount?.birthday.formatDateToClient())
        mAvatar.set(mAccount?.avatar)
        mMobile.set(mAccount?.phone.toString())
        mUsername.set(mAccount?.username)
    }

    fun editProfile(view: View){
        view.findNavController().navigate(R.id.action_ProfileFragment_to_EditProfileFragment)
    }

    fun onChangePasswordClicked(view: View){
        val action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment("CHANGE")
        view.findNavController().navigate(action)
    }

    fun uploadAvatar(path: String, onComplete: (String?) -> Unit) {
        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = userRepository.changeAvatar(path)
                if (result is Result.Success){
                    mAvatar.set(result.data.message)
                }else
                    onComplete.invoke((result is Result.Error).toString())
            }catch (ex: Exception){
                onComplete.invoke(ex.message)
            }
        }
    }
}