package com.uit.party.ui.main.payment_party

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uit.party.R
import com.uit.party.databinding.ItemDishInBillBinding
import com.uit.party.model.Dishes
import com.uit.party.util.UiUtil.toVNCurrency

class ListDishesAdapter: RecyclerView.Adapter<ListDishesAdapter.ListDishesViewHolder>(){
    val list = ArrayList<Dishes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDishesViewHolder {
        val binding = DataBindingUtil.inflate<ItemDishInBillBinding>(LayoutInflater.from(parent.context), R.layout.item_dish_in_bill, parent, false)
        return ListDishesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListDishesViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ListDishesViewHolder(private val binding: ItemDishInBillBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(dish: Dishes) {
            binding.tvNameDish.text = dish.name
            binding.tvPriceDish.text = dish.price.toString().toVNCurrency()
            binding.tvQuantityDish.text = dish.count.toString()
            Glide.with(binding.ivAvatar).load(dish.feature_image).apply { RequestOptions.centerCropTransform() }.into(binding.ivAvatar)
        }
    }

}