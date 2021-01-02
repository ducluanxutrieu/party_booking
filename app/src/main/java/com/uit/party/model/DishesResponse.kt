package com.uit.party.model

import com.google.gson.annotations.SerializedName

class DishesResponse : BaseResponse(){
    @SerializedName("data")
    val listDishes: ArrayList<DishModel>? = null
}