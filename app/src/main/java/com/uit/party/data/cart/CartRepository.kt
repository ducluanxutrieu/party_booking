package com.uit.party.data.cart

import androidx.lifecycle.LiveData
import com.uit.party.data.Result
import com.uit.party.data.PartyBookingDatabase
import com.uit.party.model.BillResponseModel
import com.uit.party.model.CartModel
import com.uit.party.model.GetPaymentResponse
import com.uit.party.model.RequestOrderPartyModel
import com.uit.party.util.ServiceRetrofit
import com.uit.party.util.SharedPrefs
import com.uit.party.util.handleRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor (
    private val networkService: ServiceRetrofit,
    private val sharedPrefs: SharedPrefs,
    database: PartyBookingDatabase
) {
    private val cartDao = database.cartDao
    val listCart: LiveData<List<CartModel>> = cartDao.getCart

    suspend fun deleteAllCart() {
        cartDao.deleteAllCart()
    }

    suspend fun orderParty(bookModel: RequestOrderPartyModel): Result<BillResponseModel> {
        try {
            return handleRequest {
                networkService.orderParty(sharedPrefs.token, bookModel)
            }
        } catch (error: Throwable) {
            throw Throwable(error)
        }
    }

    suspend fun getPayment(id: String): Result<GetPaymentResponse> {
        return handleRequest {
            networkService.getPayment(sharedPrefs.token, id)
        }
    }

    suspend fun insertCart(cartModel: CartModel) {
        try {
            val item: CartModel? = cartDao.getCartItem(cartModel.id)

            if (item != null) {
                val quantity = item.quantity + 1
                cartDao.updateQuantityCart(quantity, item.id)
            } else
                cartDao.insertCart(cartModel)
        } catch (cause: Throwable) {
            Result.Error(Exception(cause))
        }
    }

    suspend fun updateCart(cartModel: CartModel) {
        try {
            cartDao.updateQuantityCart(cartModel.quantity, cartModel.id)
        } catch (cause: Throwable) {
            Result.Error(Exception(cause))
        }
    }

    suspend fun deleteCart(cartModel: CartModel) {
        try {
            cartDao.deleteCart(cartModel)
        } catch (cause: Throwable) {
            Result.Error(Exception(cause))
        }
    }
}