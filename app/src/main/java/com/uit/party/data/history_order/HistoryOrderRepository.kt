package com.uit.party.data.history_order

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.uit.party.data.PartyBookingDatabase
import com.uit.party.util.Constants.Companion.NETWORK_PAGE_SIZE
import com.uit.party.util.ServiceRetrofit
import com.uit.party.util.SharedPrefs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryOrderRepository @Inject constructor (
    private val networkService: ServiceRetrofit,
    private val database: PartyBookingDatabase,
    private val sharedPrefs: SharedPrefs
) {

    fun getListOrdered(): Flow<PagingData<CartItem>>{
        val pagingSourceFactory = {database.historyOrderDao.pagingSource()}

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = HistoryOrderRemoteMediator(
                networkService, database, sharedPrefs.token
            )
        ).flow
    }
}