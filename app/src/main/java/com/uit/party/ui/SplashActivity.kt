package com.uit.party.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uit.party.ui.main.MainActivity
import com.uit.party.ui.sign_in.SignInActivity
import com.uit.party.user.UserManager
import com.uit.party.util.GlobalApplication

class SplashActivity : AppCompatActivity() {
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as GlobalApplication).appComponent
        userManager = appComponent.userManager()

        val intent =
            if (userManager.checkUserLoggedIn()) {
                Intent(this@SplashActivity, MainActivity::class.java)
            } else Intent(this@SplashActivity, SignInActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        // close splash activity
        finish()
    }
}