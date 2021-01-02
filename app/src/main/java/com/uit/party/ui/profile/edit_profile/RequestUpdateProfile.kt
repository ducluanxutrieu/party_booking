package com.uit.party.ui.profile.edit_profile

import com.google.gson.annotations.SerializedName

class RequestUpdateProfile(
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("fullName")
    var fullName: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("birthday")
    var birthday: String? = null,
    @SerializedName("sex")
    var sex: String? = null
)