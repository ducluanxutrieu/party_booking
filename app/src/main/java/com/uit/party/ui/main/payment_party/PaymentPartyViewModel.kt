package com.uit.party.ui.main.payment_party

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.party.data.Result
import com.uit.party.data.cart.CartRepository
import com.uit.party.model.BillModel
import com.uit.party.util.UiUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentPartyViewModel @Inject constructor (private val cartRepository: CartRepository) : ViewModel() {
    val mShowLoading = ObservableBoolean(false)
    lateinit var mBillModel: BillModel
    private val mURLPaymentLive = MutableLiveData<String>()
    val mURLPayment: LiveData<String> = mURLPaymentLive

    fun getPayment() {
        mShowLoading.set(true)
        viewModelScope.launch {
            try {
                 val result = cartRepository.getPayment(mBillModel._id)
                mShowLoading.set(false)
                if (result is Result.Success){
                    mURLPaymentLive.postValue(result.data.paymentInfo?.id)
                }else{
                    val message = result.toStrings()
                    UiUtil.showToast(message)
                }
            }catch (cause: Throwable){
                mShowLoading.set(false)
                cause.message?.let { UiUtil.showToast(it) }
            }
        }
    }
}