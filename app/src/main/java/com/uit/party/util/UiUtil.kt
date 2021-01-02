package com.uit.party.util

import android.widget.Toast
import java.text.NumberFormat
import java.util.*

object UiUtil {
    fun showToast(toast: String?) {
        val context = GlobalApplication.appContext
        if (!toast.isNullOrEmpty())
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    fun getString(stringId: Int): String {
        val context = GlobalApplication.appContext
        return context?.getString(stringId) ?: ""
    }

    fun String?.toVNCurrency(): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi"))
        return formatter.format(this?.toDouble()) + " ₫"
    }

    fun CharSequence?.getNumber(): Int {
        return when {
            this.isNullOrEmpty() -> {
                1
            }
            this.toString().toInt() < 1 -> {
                1
            }
            else -> {
                this.toString().toInt()
            }
        }
    }
}