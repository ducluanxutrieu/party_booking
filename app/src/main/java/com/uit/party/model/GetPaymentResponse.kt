package com.uit.party.model

import com.google.gson.annotations.SerializedName

class GetPaymentResponse : BaseResponse() {
    @SerializedName("data")
    val paymentInfo: PaymentInfo? = null
}

class PaymentInfo {
    @SerializedName("id")
    val id: String? = null
}