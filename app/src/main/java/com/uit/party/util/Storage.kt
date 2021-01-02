package com.uit.party.util


interface Storage {
    fun <T> setData(key: String, data: T)
    fun <T> getData(key: String, anonymousClass: Class<T>): T?
    fun clear()
}