package com.uit.party.model

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("message")
    var message: String? = null
)