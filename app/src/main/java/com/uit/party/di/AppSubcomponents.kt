package com.uit.party.di


import com.uit.party.ui.main.MenuComponent
import com.uit.party.ui.profile.ProfileComponent
import com.uit.party.ui.sign_in.SignInComponent
import com.uit.party.user.UserComponent
import dagger.Module

@Module(subcomponents = [SignInComponent::class, UserComponent::class, MenuComponent::class, ProfileComponent::class, OrderComponent::class])
class AppSubcomponents