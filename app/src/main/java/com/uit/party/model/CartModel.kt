package com.uit.party.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "cart_table")
data class CartModel(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("price_new") val newPrice: String? = null,

    var quantity: Int = 0,

    @SerializedName("feature_image") val featureImage: String? = null
) : Serializable