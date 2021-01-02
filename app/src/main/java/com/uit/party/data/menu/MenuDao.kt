package com.uit.party.data.menu

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uit.party.model.DishModel

@Dao
interface MenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: DishModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDish(list: ArrayList<DishModel>)

    @Query("DELETE FROM dish_table WHERE id = :id")
    fun deleteDish(id: String)

    @Query("DELETE FROM dish_table")
    suspend fun deleteMenu()

    @Update(entity = DishModel::class)
    suspend fun updateDish(dishModel: DishModel)

    @get:Query("SELECT * FROM dish_table")
    val allDish: LiveData<List<DishModel>>
}