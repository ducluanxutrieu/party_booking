package com.uit.party.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginModel(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String
) : Serializable

class RegisterModel(
    @SerializedName("fullName") var fullName: String,
    @SerializedName("username") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("phoneNumber") var phoneNumber: String,
    @SerializedName("password") var password: String
) : Serializable

class AccountResponse(
    @SerializedName("data") val account: Account? = null
) : BaseResponse()

class Account {
    @SerializedName("_id")
    val id : String ?= null

    @SerializedName("full_name")
    val fullName : String  ?= null

    @SerializedName("username")
    val username : String  ?= null

    @SerializedName("email")
    val email : String ?= null

    @SerializedName("phone")
    val phone : Long = 0

    @SerializedName("birthday")
    val birthday : String? = null

    @SerializedName("gender")
    val gender : Int = 0

    @SerializedName("role")
    val role : Int = 0

    @SerializedName("avatar")
    var avatar : String ?= null

    @SerializedName("country_code")
    val countryCode : Int = 0

    @SerializedName("token")
    val token : String ?= null
}

internal enum class UserRole {
    UserDeleted, Customer, Staff, Admin
}

internal enum class UserGender {
    Other, Male, Female
}