package com.uit.party.model

import com.google.gson.annotations.SerializedName

class DishItemResponse : BaseResponse(){
    @SerializedName("dish")
    val dish: DishModel? = null
}