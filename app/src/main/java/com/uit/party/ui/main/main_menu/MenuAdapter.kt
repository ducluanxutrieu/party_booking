package com.uit.party.ui.main.main_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uit.party.R
import com.uit.party.databinding.ItemMainMenuBinding
import com.uit.party.model.MenuModel

class MenuAdapter: ListAdapter<MenuModel, MenuAdapter.MenuViewHolder>(DIFF_CALLBACK){
    private lateinit var binding: ItemMainMenuBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_main_menu,
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

//    fun updateDish(dishModel: DishModel, dishType: String, position: Int) {
//        try {
//            for (row in mListMenuFiltered) {
//                if (row.menuName == dishType) {
//                    row.listDish[position] = dishModel
//                }
//            }
//        }catch (e : Exception){
//            e.printStackTrace()
//        }
//    }

    class MenuViewHolder(val binding: ItemMainMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuModel: MenuModel) {
            val itemViewModel = ItemMenuViewModel()
            binding.itemViewModel = itemViewModel
//            binding.setVariable(BR.itemViewModel, itemViewModel)
//            binding.executePendingBindings()
            itemViewModel.init(menuModel)
            binding.rvDishMain.adapter = itemViewModel.mDishesAdapter
            binding.rvDishMain.setHasFixedSize(false)
        }
    }

/*    interface OnItemClickListener{
        fun onItemClickListen(dishModel: DishModel)
    }*/

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuModel>(){
            override fun areItemsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
                return oldItem.menuName == newItem.menuName
            }

            override fun areContentsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
                return (oldItem.menuName == newItem.menuName &&
                        oldItem.listDish == newItem.listDish)
            }

        }
    }
}