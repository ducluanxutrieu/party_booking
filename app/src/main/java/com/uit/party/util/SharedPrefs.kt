package com.uit.party.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.uit.party.util.Constants.Companion.PREFS_NAME
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class SharedPrefs @Inject constructor(context: Context): Storage {
    private var mSharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)


    val token: String
    get() = SharedPrefs(GlobalApplication.appContext!!).getData(Constants.TOKEN_ACCESS_KEY, String::class.java) ?: ""

    override fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    override fun <T> setData(key: String, data: T) {
        val editor = mSharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data as String)
            is Boolean -> editor.putBoolean(key, data as Boolean)
            is Float -> editor.putFloat(key, data as Float)
            is Int -> editor.putInt(key, data as Int)
            is Long -> editor.putLong(key, data as Long)
            else -> {
                editor.putString(key, Gson().toJson(data))
            }
        }
        editor.apply()
    }

    override fun <T> getData(key: String, anonymousClass: Class<T>): T? {
        return when (anonymousClass) {
            String::class.java -> mSharedPreferences.getString(key, "") as T
            Boolean::class.java -> java.lang.Boolean.valueOf(mSharedPreferences.getBoolean(key, false)) as T
            Float::class.java -> java.lang.Float.valueOf(mSharedPreferences.getFloat(key, 0f)) as T
            Int::class.java -> Integer.valueOf(mSharedPreferences.getInt(key, 0)) as T
            Long::class.java -> java.lang.Long.valueOf(mSharedPreferences.getLong(key, 0)) as T
            else -> Gson().fromJson(mSharedPreferences.getString(key, ""), anonymousClass)
        }
    }
}
