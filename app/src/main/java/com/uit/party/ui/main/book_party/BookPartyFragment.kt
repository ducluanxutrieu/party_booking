package com.uit.party.ui.main.book_party

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uit.party.R
import com.uit.party.databinding.FragmentBookPartyBinding
import com.uit.party.model.CartModel
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.UiUtil
import com.uit.party.util.UiUtil.getNumber
import javax.inject.Inject


class BookPartyFragment : Fragment(){
    private val myArgs : BookPartyFragmentArgs by navArgs()

    @Inject
    lateinit var mViewModel: BookPartyViewModel
    private lateinit var binding: FragmentBookPartyBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).orderComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_party, container, false)
        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data = myArgs.listCart
        if (!data.isNullOrEmpty()) {
            val listType = object : TypeToken<List<CartModel?>?>() {}.type
            val list: List<CartModel> = Gson().fromJson(data, listType)
            mViewModel.listCartStorage = list
        }

        mViewModel.setTotalPrice()

        binding.etCustomer.doOnTextChanged { text, _, _, _ ->
            val num = text.getNumber()
            if (text != num.toString()) {
                binding.etCustomer.setText(num.toString())
            }
            mViewModel.mNumberCustomer = num
        }

        binding.etTable.doOnTextChanged { text, _, _, _ ->
            val num = text.getNumber()
            if (text != num.toString()) {
                binding.etTable.text = SpannableStringBuilder(num.toString())
            }
            mViewModel.mNumberTable = num
            mViewModel.setTotalPrice()
        }

        listenLiveData()
    }

    private fun listenLiveData(){
        mViewModel.messageLive.observe(viewLifecycleOwner, {
            UiUtil.showToast(it.second)
            if (it.first != null) {
                val action =
                    BookPartyFragmentDirections.actionBookingPartyFragmentToPaymentFragment(
                        it.first
                    )
                this.findNavController().navigate(action)
            }
        })
    }
}