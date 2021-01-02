package com.uit.party.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BillResponseModel : BaseResponse() {
    @SerializedName("data")
    val billModel: BillModel? = null
}


data class BillModel(
    @SerializedName("date_party") val date_party : String,
    @SerializedName("dishes") val dishes : List<Dishes>,
    @SerializedName("count_customer") val count_customer : Int,
    @SerializedName("table") val table : Int,
    @SerializedName("total") val total : Int,
    @SerializedName("customer") val customer : String,
    @SerializedName("create_at") val create_at : String,
    @SerializedName("confirm_status") val confirm_status : Int,
    @SerializedName("confirm_at") val confirm_at : String,
    @SerializedName("confirm_by") val confirm_by : String,
    @SerializedName("confirm_note") val confirm_note : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("payment_status") val payment_status : Int,
    @SerializedName("payment_type") val payment_type : Int,
    @SerializedName("payment_at") val payment_at : String,
    @SerializedName("payment_by") val payment_by : String,
    @SerializedName("_id") val _id : String
): Serializable

data class Dishes (

    @SerializedName("_id") val _id : String,
    @SerializedName("count") val count : Int,
    @SerializedName("name") val name : String,
    @SerializedName("feature_image") val feature_image : String,
    @SerializedName("price") val price : Int,
    @SerializedName("discount") val discount : Int,
    @SerializedName("currency") val currency : String,
    @SerializedName("total_money") val total_money : Int
)