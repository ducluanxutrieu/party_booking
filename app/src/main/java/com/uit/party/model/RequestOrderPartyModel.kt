package com.uit.party.model

import com.google.gson.annotations.SerializedName

class RequestOrderPartyModel (
    @SerializedName("date_party")
    val dateParty: String,

    @SerializedName("table")
    val numberTable: Int,

    @SerializedName("count_customer")
    val numberCustomer: Int,

    @SerializedName("discount_code")
    val discountCode: String?,

    @SerializedName("dishes")
    val listDishes: ArrayList<ListDishes>
)

class ListDishes(
    @SerializedName("count")
    val numberDish: Int,

    @SerializedName("_id")
    val id: String
)