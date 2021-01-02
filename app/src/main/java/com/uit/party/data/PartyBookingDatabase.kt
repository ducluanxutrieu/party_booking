package com.uit.party.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uit.party.data.cart.CartDao
import com.uit.party.data.history_order.CartItem
import com.uit.party.data.history_order.HistoryOrderDao
import com.uit.party.data.menu.MenuDao
import com.uit.party.data.rate.RateDishDao
import com.uit.party.data.rate.RateRemoteKeysDao
import com.uit.party.model.*


/**
 * TitleDatabase provides a reference to the dao to repositories
 */
@TypeConverters(ImageConverter::class)
@Database(entities = [DishModel::class, CartModel::class, RateModel::class, RateRemoteKeys::class, CartItem::class], version = 6, exportSchema = false)
abstract class PartyBookingDatabase : RoomDatabase() {
    abstract val menuDao: MenuDao
    abstract val rateDao: RateDishDao
    abstract val rateRemoteDao: RateRemoteKeysDao
    abstract val cartDao: CartDao
    abstract val historyOrderDao: HistoryOrderDao
}

private lateinit var INSTANCE: PartyBookingDatabase

/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): PartyBookingDatabase {
    synchronized(PartyBookingDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    PartyBookingDatabase::class.java,
                    "party_booking_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
