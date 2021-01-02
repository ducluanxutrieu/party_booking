package com.uit.party.data.history_order

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface HistoryOrderDao {

    /*@Query("SELECT * FROM history_order_table")
    suspend fun getHistoryOrder() :HistoryCartModel

    @Query("DELETE FROM history_order_table")
    suspend fun clearAllHistoryOrder()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryCart(cartModel: HistoryCartModel)*/

    @Query("DELETE FROM item_ordered_table")
    suspend fun clearAllOrdered()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListHistoryOrder(list: List<CartItem>)

    @Query("SELECT * FROM item_ordered_table ORDER BY createAt DESC")
    fun pagingSource(): PagingSource<Int, CartItem>

}