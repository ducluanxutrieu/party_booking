package com.uit.party.model

import com.google.gson.annotations.SerializedName

class UpdateDishResponse : BaseResponse() {
    @SerializedName("dish")
    val dish: DishModel? = null
}