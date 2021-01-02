package com.uit.party.ui.main.main_menu.menu_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.uit.party.BR
import com.uit.party.R
import com.uit.party.databinding.ItemDishBinding
import com.uit.party.model.DishModel
import com.uit.party.ui.main.main_menu.MenuFragmentDirections
import com.uit.party.util.rxbus.RxBus
import com.uit.party.util.rxbus.RxEvent

class DishesAdapter : RecyclerView.Adapter<DishesAdapter.DishViewHolder>(){
    private var dishList = ArrayList<DishModel>()
    private lateinit var binding: ItemDishBinding
    private lateinit var mDishType: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_dish,
            parent,
            false
        )
        return DishViewHolder(mDishType, binding)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    fun setDishesType(dishType: String) {
        mDishType = dishType
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bind(dishList[position])
    }

    fun setData(items: ArrayList<DishModel>) {
        dishList.clear()
        dishList = items
        notifyDataSetChanged()
    }

    class DishViewHolder(private val dishType: String, val binding: ItemDishBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dishModel: DishModel) {
            val itemViewModel = ItemDishViewModel()
            binding.setVariable(BR.itemViewModel, itemViewModel)
            binding.executePendingBindings()
            itemViewModel.init(dishModel)
            binding.root.setOnClickListener {
                val action = MenuFragmentDirections.actionListDishFragmentToDishDetailFragment(absoluteAdapterPosition, dishType, dishModel)
                it.findNavController().navigate(action)
            }
            binding.btnAddToCard.setOnClickListener {
                RxBus.publish(RxEvent.AddToCart(dishModel))
            }
        }
    }
}