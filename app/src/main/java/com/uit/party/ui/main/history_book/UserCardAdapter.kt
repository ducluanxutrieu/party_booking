package com.uit.party.ui.main.history_book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uit.party.R
import com.uit.party.data.history_order.CartItem
import com.uit.party.databinding.ItemUserCardBinding
import com.uit.party.util.TimeFormatUtil.formatTime12hToClient
import com.uit.party.util.UiUtil.toVNCurrency

class UserCardAdapter : PagingDataAdapter<CartItem, UserCardAdapter.UserCardViewHolder>(DIFF_CALLBACK){

    class UserCardViewHolder(private val binding: ItemUserCardBinding): RecyclerView.ViewHolder(binding.root){
        private val itemDishAdapter = ItemDishUserCartAdapter()
        private var isExpand = false

        fun bindData(userCart: CartItem) {
            binding.tvTimeBooking.text = userCart.dateParty.formatTime12hToClient()
            binding.tvNumberTableBooking.text = userCart.table.toString()
            binding.tvTotalPrice.text = userCart.total.toString().toVNCurrency()
//            itemDishAdapter.setData(userCart.dishes)
            binding.rvListDishes.adapter = itemDishAdapter
            binding.rvListDishes.hasFixedSize()

            itemView.setOnClickListener {
                if (isExpand)
                {
                    binding.llListDishes.visibility = View.GONE
                }else{
                    binding.llListDishes.visibility = View.VISIBLE
                }
                isExpand = !isExpand
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder {
        val binding: ItemUserCardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_user_card, parent, false)
        return UserCardViewHolder(binding)
    }


    override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) {
         getItem(position)?.let { holder.bindData(it) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return (oldItem.dateParty == newItem.dateParty &&
                        oldItem.customer == newItem.customer &&
                        oldItem.total == newItem.total)
            }

        }
    }
}