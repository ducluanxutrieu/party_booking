package com.uit.party.ui.main.cart_detail

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.uit.party.R
import com.uit.party.databinding.FragmentCartDetailBinding
import com.uit.party.model.CartModel
import com.uit.party.ui.main.MainActivity
import javax.inject.Inject


class CartDetailFragment : Fragment(), OnCartDetailListener {
    private lateinit var mBinding: FragmentCartDetailBinding

    @Inject
    lateinit var mViewModel: CartDetailViewModel

    val mCartAdapter = CartDetailAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).orderComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cart_detail, container, false)
        listenLiveData()
        return mBinding.root
    }

    private fun listenLiveData() {
        mViewModel.listCart.observe(viewLifecycleOwner, {
            mCartAdapter.submitList(it)
            mCartAdapter.notifyDataSetChanged()
            mViewModel.listCartStorage = it
            mViewModel.mShowCart.set(it.isNotEmpty())
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.viewModel = mViewModel

        mBinding.rvCartDetail.adapter = mCartAdapter
        val itemTouchHelper = ItemTouchHelper(enableSwipe())
        itemTouchHelper.attachToRecyclerView(mBinding.rvCartDetail)
        val itemDecoder = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        mBinding.rvCartDetail.addItemDecoration(itemDecoder)
    }

    private fun enableSwipe(): ItemTouchHelper.SimpleCallback {
        val icon: Drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_forever)!!
        val background = ColorDrawable(Color.RED)

        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val cardModel = mCartAdapter.getSingleItem(position)
                mViewModel.onDeleteItemCart(cardModel)
                val snackBar = Snackbar.make(view!!, "Removed a dish.", Snackbar.LENGTH_LONG)
                snackBar.setAction("UNDO") { mViewModel.insertCart(cardModel) }
                snackBar.setActionTextColor(Color.YELLOW)
                snackBar.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight

                when {
                    dX > 0 -> { // Swiping to the right
                        val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
                        val iconRight = itemView.left + iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset,
                            itemView.bottom
                        )
                    }
                    dX < 0 -> { // Swiping to the left
                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                    else -> // view is unSwiped
                        background.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                icon.draw(c)
            }
        }
    }

    override fun onChangeNumberDish(cartModel: CartModel, isIncrease: Boolean) {
        if (isIncrease) {
            mViewModel.changeQuantityCart(cartModel.apply { ++quantity })
        } else {
            if (cartModel.quantity > 0) {
                mViewModel.changeQuantityCart(cartModel.apply { --quantity})
            } else mViewModel.changeQuantityCart(cartModel.apply { quantity = 0 })
        }
    }
}