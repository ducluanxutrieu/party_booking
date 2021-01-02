package com.uit.party.util

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatUtil {
    private const val formatServer = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val formatClient = "dd-MM-yyyy"
    private const val formatTimeClient12h = "dd/MM/yyyy HH:mm"
    private val sdfServer = SimpleDateFormat(formatServer, Locale.US)
    private val sdfClient = SimpleDateFormat(formatClient, Locale.US)
    private val sdfTimeClient12h = SimpleDateFormat(formatTimeClient12h, Locale.US)

    fun String?.formatDateToServer(): String? {
        if (this != null) {
            val dateOrigin = sdfClient.parse(this)
            return sdfServer.format(dateOrigin!!)
        }
        return null
    }

    fun String?.formatDateToClient(): String? {
        if (this != null) {
            val dateOrigin = sdfServer.parse(this) ?: return ""
            return sdfClient.format(dateOrigin)
        }
        return null
    }

    fun Calendar.formatTimeToServer(): String {
        val myFormat = "MM/dd/yyyy HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(this.time)
    }

    fun Calendar.formatTimeToClient(): String {
        val sdf = SimpleDateFormat(formatTimeClient12h, Locale.US)
        return sdf.format(this.time)
    }

    fun String?.formatTime12hToClient(): String? {
        if (this != null) {
            val dateOrigin = sdfServer.parse(this)
            return sdfTimeClient12h.format(dateOrigin!!)
        }
        return null
    }
}