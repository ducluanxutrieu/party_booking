package com.uit.party.model

import com.google.gson.annotations.SerializedName

class RequestRatingModel(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("score")
    val score: Double? = 0.0,

    @SerializedName("comment")
    val comment: String? = null
)