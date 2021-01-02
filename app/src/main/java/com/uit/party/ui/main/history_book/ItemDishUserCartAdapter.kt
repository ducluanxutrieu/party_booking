package com.uit.party.ui.main.history_book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.uit.party.R
import com.uit.party.databinding.ItemDishUserCartBinding
import com.uit.party.data.history_order.DishCart

class ItemDishUserCartAdapter : RecyclerView.Adapter<ItemDishUserCartAdapter.UserCardViewHolder>(){
    private val mListDishes =  ArrayList<DishCart>()

    class UserCardViewHolder(private val binding: ItemDishUserCartBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(dishCart: DishCart) {
            binding.tvDishesNumber.text = dishCart.count.toString()
            binding.tvNameDish.text = dishCart.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder {
        val binding: ItemDishUserCartBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_dish_user_cart, parent, false)
        return UserCardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mListDishes.size
    }

    override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) {
        holder.bindData(mListDishes[position])
    }

    fun setData(items: ArrayList<DishCart>) {
        mListDishes.clear()
        mListDishes.addAll(items)
        notifyDataSetChanged()
    }
}