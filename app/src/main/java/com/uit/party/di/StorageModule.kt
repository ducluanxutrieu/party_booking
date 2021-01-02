package com.uit.party.di

import android.content.Context
import com.uit.party.data.PartyBookingDatabase
import com.uit.party.data.getDatabase
import com.uit.party.util.ServiceRetrofit
import com.uit.party.util.SharedPrefs
import com.uit.party.util.Storage
import com.uit.party.util.getNetworkService
import dagger.Module
import dagger.Provides


/*@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class SignInStorage

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class NetworkService*/

@Module
class StorageModule {

//    @SignInStorage
    @Provides
    fun provideSignInStorage(context: Context): Storage {
        return SharedPrefs(context)
    }

//    @NetworkService
    @Provides
    fun provideNetworkService(): ServiceRetrofit{
        return getNetworkService()
    }

    @Provides
    fun providePartyDatabase(context: Context): PartyBookingDatabase{
        return getDatabase(context)
    }
}