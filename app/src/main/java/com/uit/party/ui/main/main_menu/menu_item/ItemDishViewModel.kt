package com.uit.party.ui.main.main_menu.menu_item

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.uit.party.model.DishModel
import com.uit.party.util.UiUtil.toVNCurrency

class ItemDishViewModel : BaseObservable(){
    var imageDish = ObservableField<String>()
    var priceDish = ObservableField<String>()
    var nameDish = ObservableField<String>()
    private lateinit var mDishModel: DishModel

    fun init(dishModel: DishModel){
        imageDish.set(dishModel.featureImage)
        priceDish.set(dishModel.price?.toVNCurrency())
        nameDish.set(dishModel.name)
        mDishModel = dishModel
    }
}