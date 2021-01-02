package com.uit.party.ui.main.detail_dish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.smarteist.autoimageslider.SliderViewAdapter
import com.uit.party.R
import com.uit.party.databinding.ItemImageDishDetailBinding

class DishDetailAdapter : SliderViewAdapter<DishDetailAdapter.DishDetailViewHolder>()
{
    private var mListData = ArrayList<String>()

    override fun onBindViewHolder(holder: DishDetailViewHolder, position: Int) {
        holder.bindData(mListData[position])
    }

    fun setData(items: ArrayList<String>) {
        mListData = items
//        if (!mListData.isNullOrEmpty()) mListData.removeAt(0)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): DishDetailViewHolder {
        val binding = DataBindingUtil.inflate<ItemImageDishDetailBinding>(LayoutInflater.from(parent.context), R.layout.item_image_dish_detail, parent, false)
        return DishDetailViewHolder(binding)
    }

    override fun getCount(): Int {
        return mListData.size
    }

    class DishDetailViewHolder(private val mBinder: ItemImageDishDetailBinding): SliderViewAdapter.ViewHolder(mBinder.root){
        fun bindData(url: String) {
            Glide.with(mBinder.root.context).load(url).apply { RequestOptions.centerCropTransform() }.error(R.drawable.dish_sample).into(mBinder.ivDish)
        }
    }
}