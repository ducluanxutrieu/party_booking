package com.uit.party.util

import android.app.Application
import android.content.Context
import com.uit.party.di.AppComponent
import com.uit.party.di.DaggerAppComponent

open class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object{
        var appContext: Context? = null
            private set
    }

    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }

/*    open fun initializeComponent(): AppComponent{
        return DaggerAppComponent.factory().create(applicationContext)
    }*/
}