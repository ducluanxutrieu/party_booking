package com.uit.party.data.rate

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uit.party.model.RateModel

@Dao
interface RateDishDao {
    @Query("SELECT * FROM rate_model WHERE id_dish = :dishID")
    fun getRateByDishId(dishID: String): PagingSource<Int, RateModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemRating(rateModel: RateModel)

    @Query("DELETE FROM rate_model")
    suspend fun clearRateDishList()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRate(rateModel: List<RateModel>)
}