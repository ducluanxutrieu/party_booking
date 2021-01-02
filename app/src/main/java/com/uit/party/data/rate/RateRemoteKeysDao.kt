package com.uit.party.data.rate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uit.party.model.RateRemoteKeys

@Dao
interface RateRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRateRemoteKeys(rateRemoteKeys: List<RateRemoteKeys>)

    @Query("SELECT * FROM rate_remote_keys WHERE rateId = :rateId")
    suspend fun rateRemoteKeysRateId(rateId: String): RateRemoteKeys?

    @Query("DELETE FROM rate_remote_keys")
    suspend fun clearRateRemoteKeys()
}