package com.uit.party.ui.main.cart_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uit.party.BR
import com.uit.party.R
import com.uit.party.databinding.ItemCartDetailBinding
import com.uit.party.model.CartModel

interface OnCartDetailListener {
    fun onChangeNumberDish(cartModel: CartModel, isIncrease: Boolean)
}

class CartDetailAdapter(private val mListener: OnCartDetailListener) :
    ListAdapter<CartModel, CartDetailAdapter.CartDetailViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartDetailViewHolder {
        val binder = DataBindingUtil.inflate<ItemCartDetailBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_detail,
            parent,
            false
        )
        return CartDetailViewHolder(mListener, binder)
    }

    override fun onBindViewHolder(holder: CartDetailViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    fun getSingleItem(position: Int): CartModel {
        return getItem(position)
    }

    class CartDetailViewHolder(
        private val mListener: OnCartDetailListener,
        private val mBinder: ItemCartDetailBinding
    ) : RecyclerView.ViewHolder(mBinder.root) {
        private val mItemViewModel = CartDetailItemViewModel()

        fun bindData(cartModel: CartModel) {
            mBinder.setVariable(BR.itemViewModel, mItemViewModel)
            mBinder.executePendingBindings()
            mItemViewModel.initItemData(cartModel)
            mBinder.btnReduction.setOnClickListener {
                mListener.onChangeNumberDish(cartModel, false)
            }

            mBinder.btnIncrease.setOnClickListener {
                mListener.onChangeNumberDish(cartModel, true)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartModel>() {
            override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
                return (oldItem.quantity == newItem.quantity &&
                        oldItem.name == newItem.name)
            }

        }
    }
}