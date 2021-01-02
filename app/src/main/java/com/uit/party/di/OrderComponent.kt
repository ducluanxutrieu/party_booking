package com.uit.party.di

import com.uit.party.ui.main.MainActivity
import com.uit.party.ui.main.book_party.BookPartyFragment
import com.uit.party.ui.main.cart_detail.CartDetailFragment
import com.uit.party.ui.main.history_book.HistoryBookingFragment
import com.uit.party.ui.main.payment_party.PaymentPartyFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface OrderComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): OrderComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: BookPartyFragment)
    fun inject(fragment: CartDetailFragment)
    fun inject(fragment: HistoryBookingFragment)
    fun inject(fragment: PaymentPartyFragment)
}