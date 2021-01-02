package com.uit.party.data.cart

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uit.party.model.CartModel

@Dao
interface CartDao{
    @Query("SELECT * FROM cart_table WHERE id = :id")
    suspend fun getCartItem(id: String): CartModel?

    @Query("UPDATE cart_table SET quantity=:quantity WHERE id = :id")
    suspend fun updateQuantityCart(quantity: Int, id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartModel: CartModel)

    @Delete
    suspend fun deleteCart(cartModel: CartModel)

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCart()

    @get:Query("SELECT * FROM cart_table WHERE quantity > 0")
    val getCart: LiveData<List<CartModel>>
}