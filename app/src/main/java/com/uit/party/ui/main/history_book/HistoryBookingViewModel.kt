package com.uit.party.ui.main.history_book

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uit.party.data.history_order.CartItem
import com.uit.party.data.history_order.HistoryOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryBookingViewModel @Inject constructor (private val repository: HistoryOrderRepository) : ViewModel(){
    val mShowLoading = ObservableBoolean(false)

    fun getHistoryOrdered(): Flow<PagingData<CartItem>> {
        return repository.getListOrdered()
            .cachedIn(viewModelScope)
    }
}