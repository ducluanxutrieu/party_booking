/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uit.party.data.rate

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.uit.party.data.PartyBookingDatabase
import com.uit.party.model.RateModel
import com.uit.party.model.RateRemoteKeys
import com.uit.party.util.Constants.Companion.STARTING_PAGE_INDEX
import com.uit.party.util.ServiceRetrofit
import retrofit2.HttpException
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination

@OptIn(ExperimentalPagingApi::class)
class RateRemoteMediator(
    private val dishID: String,
    private val service: ServiceRetrofit,
    private val repoDatabase: PartyBookingDatabase
) : RemoteMediator<Int, RateModel>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, RateModel>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    Log.e("Github Remote Mediator", "PreEnd Remote key and the prevKey should not be null")
                    remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                // If the previous key is null, then we can't request more data
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    Log.e("Github Remote Mediator", "AppEnd Remote key and the prevKey should not be null")
                }
                remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = false)
            }

        }


        try {
            val apiResponse = service.getDishRates(dishID, page)

            val repos = apiResponse.itemDishRateModel?.listRatings
            val endOfPaginationReached = (repos?.isEmpty() ?: true || page == apiResponse.itemDishRateModel?.total_page)
            repoDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    repoDatabase.rateRemoteDao.clearRateRemoteKeys()
                    repoDatabase.rateDao.clearRateDishList()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos?.map {
                    RateRemoteKeys(rateId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { repoDatabase.rateRemoteDao.insertAllRateRemoteKeys(it) }
                repos?.let { repoDatabase.rateDao.insertAllRate(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RateModel>): RateRemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                repoDatabase.rateRemoteDao.rateRemoteKeysRateId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RateModel>): RateRemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                repoDatabase.rateRemoteDao.rateRemoteKeysRateId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RateModel>
    ): RateRemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repoDatabase.rateRemoteDao.rateRemoteKeysRateId(repoId)
            }
        }
    }

}