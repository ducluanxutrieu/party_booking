package com.uit.party.ui.main.cart_detail

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.uit.party.model.CartModel

class CartDetailItemViewModel : ViewModel(){
    val mAvatarDish = ObservableField("")
    val mNameDish = ObservableField("")
    val mNumberDishInType = ObservableField("1")
    val mDishPrice = ObservableField("0 Đ")

    fun initItemData(cartModel: CartModel) {
        mNumberDishInType.set(cartModel.quantity.toString())
        mNameDish.set(cartModel.name)
        mAvatarDish.set(cartModel.featureImage)
        mDishPrice.set("${cartModel.quantity * (cartModel.price?: "0").toInt()} Đ")
    }
}