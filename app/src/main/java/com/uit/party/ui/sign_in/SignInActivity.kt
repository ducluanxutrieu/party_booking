package com.uit.party.ui.sign_in

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.uit.party.R
import com.uit.party.databinding.ActivitySignInBinding
import com.uit.party.user.UserManager
import com.uit.party.util.GlobalApplication
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding

    lateinit var signInComponent: SignInComponent
    @Inject
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        signInComponent = (application as GlobalApplication).appComponent.signInComponent().create()
        signInComponent.inject(this)

        userManager = (application as GlobalApplication).appComponent.userManager()

        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
    }
}

sealed class LoginViewState
object LoginSuccess : LoginViewState()
object LoginError : LoginViewState()
