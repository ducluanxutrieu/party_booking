package com.uit.party.model

import com.google.gson.annotations.SerializedName

data class UpdateDishRequestModel(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("discount")
    val discount: String = "0"
)