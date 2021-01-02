package com.uit.party.ui.main.cart_detail

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.uit.party.data.cart.CartRepository
import com.uit.party.model.CartModel
import com.uit.party.util.UiUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartDetailViewModel @Inject constructor (private val repository: CartRepository) : ViewModel(){
    val mShowCart = ObservableBoolean(false)

    val listCart = repository.listCart
    var listCartStorage = emptyList<CartModel>()

    fun onOrderNowClicked(view: View){
        val argument = Gson().toJson(listCartStorage)
        val action = CartDetailFragmentDirections.actionCartDetailFragmentToBookingSuccessFragment(argument)
        view.findNavController().navigate(action)
    }

    fun changeQuantityCart(cartModel: CartModel) {
        viewModelScope.launch {
            try {
                repository.updateCart(cartModel)
            }catch (e: Exception){
                e.message?.let { UiUtil.showToast(it) }
            }
        }
    }

    fun onDeleteItemCart(cartModel: CartModel) {
        viewModelScope.launch {
            try {
                repository.deleteCart(cartModel)
            }catch (e: Exception){
                e.message?.let { UiUtil.showToast(it) }
            }
        }
    }

    fun insertCart(cartModel: CartModel) {
        viewModelScope.launch {
            try {
                repository.insertCart(cartModel)
            }catch (e: Exception){
                e.message?.let { UiUtil.showToast(it) }
            }
        }
    }
}