package com.uit.party.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class AddDishResponse : BaseResponse() {
    @SerializedName("dish")
    val dish: DishModel? = null
}

@Entity(tableName = "dish_table")
data class DishModel(
    @PrimaryKey
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("price_new") val newPrice: String? = null,
    @SerializedName("discount") val discount: Int? = 0,
    @SerializedName("description") val description: String? = null,

    @TypeConverters(ImageConverter::class)
    @SerializedName("categories")
    val categories: List<String>,

    @TypeConverters(ImageConverter::class)
    @SerializedName("image") val image: List<String>,
    @SerializedName("feature_image") val featureImage: String? = null,
    @SerializedName("updateAt") val updateAt: String? = null,
    @SerializedName("createAt") val createAt: String? = null
) : Serializable

class ImageConverter {
    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        return  Gson().toJson(list)
    }
}