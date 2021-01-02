package com.uit.party.util

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

class Constants {
    companion object {
        const val PREFS_NAME = "com.uit.party"
        const val REGISTERED_USER = "registered_user"
        const val TOKEN_ACCESS_KEY = "TOKEN_ACCESS"
        const val USER_INFO_KEY = "USER_INFO_KEY"
        const val STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 5

        val coroutineIO = Dispatchers.IO + coroutineExceptionHandler

        private val coroutineExceptionHandler: CoroutineExceptionHandler
            get() {
                return CoroutineExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                    Log.e("CoroutineException: ", throwable.message ?: "")
                }
            }
    }
}