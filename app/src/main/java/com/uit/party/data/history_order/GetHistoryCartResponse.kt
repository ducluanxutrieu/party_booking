package com.uit.party.data.history_order

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.uit.party.model.BaseResponse

class GetHistoryCartResponse(
    @SerializedName("data")
    val historyCartModel: HistoryCartModelResponse
) : BaseResponse()

data class HistoryCartModelResponse (
    @SerializedName("total_page")
    var totalPage: Int,

    @SerializedName("start")
    var start: Int,

    @SerializedName("end")
    var end: Int,

    @SerializedName("value")
    val cartItems: ArrayList<CartItem> = ArrayList(),
)

@Entity(tableName = "item_ordered_table")
data class CartItem (
    @PrimaryKey
    @SerializedName("_id")
    val id: String,

//    @SerializedName("dishes")
//    val dishes: ArrayList<DishCart> = ArrayList()

    @SerializedName("table")
    val table: Int? = 0,

    @SerializedName("date_party")
    val dateParty: String? = null,

    @SerializedName("count_customer")
    val countCustomer: Int? = 0,

    @SerializedName("total")
    val total: Int? = 0,

    @SerializedName("customer")
    val customer: String? = null,

    @SerializedName("create_at")
    val createAt: String? = null,

    @SerializedName("confirm_status")
    val confirmStatus: Int? = 0,

    @SerializedName("confirm_at")
    val confirmAt: String? = null,

    @SerializedName("confirm_by")
    val confirmBy: String? = null,

    @SerializedName("confirm_note")
    val confirmNote: String? = null,

    @SerializedName("currency")
    val currency: String? = null,

    @SerializedName("payment_status")
    val paymentStatus: Int? = null,

    @SerializedName("payment_type")
    val paymentType: Int? = null,

    @SerializedName("payment_at")
    val paymentAt: String? = null,

    @SerializedName("payment_by")
    val paymentBy: String? = null,
)

class DishCart {
    @SerializedName("_id")
    val id: String? = null

    @SerializedName("count")
    val count: Int = 0

    @SerializedName("name")
    val name: String? = null

    @SerializedName("feature_image")
    val featureImage: String? = null

    @SerializedName("price")
    val price: Int = 0

    @SerializedName("discount")
    val discount: Int = 0

    @SerializedName("currency")
    val currency: String? = null

    @SerializedName("total_money")
    val totalMoney: Int = 0
}