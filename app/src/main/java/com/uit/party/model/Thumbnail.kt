package com.uit.party.model

import com.uit.party.R
import com.uit.party.util.UiUtil.getString

enum class Thumbnail(val dishName: String, val image: Int) {
    Thumbnail1(getString(R.string.holiday_offers), R.drawable.holiday_offers),
    Thumbnail2(getString(R.string.first_dishes), R.drawable.first_dishes),
    Thumbnail3(getString(R.string.main_dishes), R.drawable.main_dishes),
    Thumbnail4(getString(R.string.seafood), R.drawable.seafood),
    Thumbnail5(getString(R.string.drinks), R.drawable.drinks),
    Thumbnail6(getString(R.string.dessert), R.drawable.dessert);
}
