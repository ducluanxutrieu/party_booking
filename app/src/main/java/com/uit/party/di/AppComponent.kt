package com.uit.party.di

import android.content.Context
import com.uit.party.ui.main.MenuComponent
import com.uit.party.ui.profile.ProfileComponent
import com.uit.party.ui.sign_in.SignInComponent
import com.uit.party.user.UserManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Definition of a Dagger component
@Singleton
@Component(modules = [StorageModule::class, AppSubcomponents::class])
interface AppComponent {
    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun userManager(): UserManager

    fun signInComponent(): SignInComponent.Factory
    fun menuComponent(): MenuComponent.Factory
    fun profileComponent(): ProfileComponent.Factory
    fun orderComponent(): OrderComponent.Factory

//    fun inject(activity: MainActivity)
//    fun inject(activity: SettingsActivity)
}