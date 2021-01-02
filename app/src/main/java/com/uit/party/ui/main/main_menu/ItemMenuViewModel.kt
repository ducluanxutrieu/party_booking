package com.uit.party.ui.main.main_menu

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.uit.party.model.MenuModel
import com.uit.party.ui.main.main_menu.menu_item.DishesAdapter

class ItemMenuViewModel : ViewModel(){
    lateinit var mDishesAdapter: DishesAdapter
    val mTypeMenuField = ObservableField("")

    fun init(menuModel: MenuModel) {
        mDishesAdapter = DishesAdapter()
        mDishesAdapter.setDishesType(menuModel.menuName)
        mDishesAdapter.setData(menuModel.listDish)
        mTypeMenuField.set(menuModel.menuName)
    }
}