package com.uit.party.ui.main.main_menu

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.uit.party.data.Result
import com.uit.party.data.cart.CartRepository
import com.uit.party.data.menu.MenuRepository
import com.uit.party.model.CartModel
import com.uit.party.model.DishModel
import com.uit.party.model.MenuModel
import com.uit.party.user.UserDataRepository
import com.uit.party.user.UserManager
import com.uit.party.util.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val cartRepository: CartRepository,
    private val userDataRepository: UserDataRepository,
    private val userManager: UserManager
) : ViewModel() {
    val mShowFab = ObservableInt(View.GONE)
    val mShowLoading = ObservableBoolean(false)
    val mShowMenu = ObservableBoolean(false)

    val listMenu: LiveData<List<DishModel>> = repository.listMenu
    private val _logoutState = MutableLiveData<String>()
    val logoutState: LiveData<String> = _logoutState

    init {
        getListDishes()
        setIsAdmin()
    }

    fun getListDishes() {
        viewModelScope.launch(Constants.coroutineIO) {
            try {
                repository.getListDishes()
            } catch (error: Exception) {
                _logoutState.postValue(error.message)
            }
        }
    }

    fun logout() {
        mShowLoading.set(true)

        viewModelScope.launch(Constants.coroutineIO) {
            try {
                val result = userDataRepository.logout()
                if (result is Result.Success) {
                    _logoutState.postValue("")
                } else {
                    _logoutState.postValue((result as Result.Error).exception.toString())
                }
            } catch (cause: Throwable) {
                Result.Error(Exception(cause))
            } finally {
                mShowLoading.set(false)
            }
        }
    }

    private fun setIsAdmin() {
        if (userManager.isAdmin) {
            mShowFab.set(View.VISIBLE)
        }
    }

    fun onAddDishClicked(view: View) {
        val action = MenuFragmentDirections.actionListDishToAddDish(0, "", null)
        view.findNavController().navigate(action)
    }

    fun menuAllocation(dishes: List<DishModel>): ArrayList<MenuModel> {
        val listMenu = ArrayList<MenuModel>()
        val listHolidayOffers = ArrayList<DishModel>()
        val listFirstDishes = ArrayList<DishModel>()
        val listMainDishes = ArrayList<DishModel>()
        val listSeafood = ArrayList<DishModel>()
        val listDrink = ArrayList<DishModel>()
        val listDessert = ArrayList<DishModel>()
        for (row in dishes) {
            when (row.categories[0]) {
                "Holiday Offers" -> listHolidayOffers.add(row)
                "First Dishes" -> listFirstDishes.add(row)
                "Main Dishes" -> listMainDishes.add(row)
                "Seafood" -> listSeafood.add(row)
                "Drinks" -> listDrink.add(row)
                "Dessert" -> listDessert.add(row)
            }
        }
        if (listHolidayOffers.size > 0) {
            listMenu.add(MenuModel("Holiday Offers", listHolidayOffers))
        }
        if (listFirstDishes.size > 0) {
            listMenu.add(MenuModel("First Dishes", listFirstDishes))
        }
        if (listMainDishes.size > 0) {
            listMenu.add(MenuModel("Main Dishes", listMainDishes))
        }
        if (listSeafood.size > 0) {
            listMenu.add(MenuModel("Seafood", listSeafood))
        }
        if (listDrink.size > 0) {
            listMenu.add(MenuModel("Drinks", listDrink))
        }

        if (listDessert.size > 0) {
            listMenu.add(MenuModel("Dessert", listDessert))
        }

        return listMenu
    }

    fun addDishToCart(cartModel: CartModel) {
        viewModelScope.launch {
            cartRepository.insertCart(cartModel)
        }
    }
}
