package com.uit.party.user

import dagger.Subcomponent


@LoggedUserScope
@Subcomponent
interface UserComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): UserComponent
    }

//    fun inject(activity: MainActivity)
//    fun inject(activity: SettingsActivity)
}