package com.uit.party.ui.main.payment_party

//import com.uit.party.databinding.PaymentPartyFragmentBinding
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.uit.party.R
import com.uit.party.databinding.PaymentPartyFragmentBinding
import com.uit.party.model.Dishes
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.TimeFormatUtil.formatTime12hToClient
import com.uit.party.util.UiUtil.toVNCurrency
import javax.inject.Inject

class PaymentPartyFragment : Fragment() {
    private lateinit var mBinding: PaymentPartyFragmentBinding

    @Inject
    lateinit var viewModel: PaymentPartyViewModel
    private val myArgs: PaymentPartyFragmentArgs by navArgs()
    private val mAdapter = ListDishesAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).orderComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.payment_party_fragment, container, false)
        mBinding.viewModel = viewModel
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        setupListener()
    }

    private fun initData() {
        val model = myArgs.billModel

        if (model == null) {
            mBinding.root.findNavController().popBackStack()
        } else
            viewModel.mBillModel = model


        mBinding.tvCustomerName.text = model?.customer
        mBinding.tvTotalBill.text = model?.total.toString().toVNCurrency()
        mBinding.tvNumberTable.text = model?.table.toString()
        mBinding.tvTimeBooking.text = model?.date_party.formatTime12hToClient()
        model?.dishes?.let { setupRecyclerView(it) }
    }

    private fun setupListener() {
        mBinding.btPayment.setOnClickListener {
            viewModel.getPayment()
        }

        viewModel.mURLPayment.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val url = "https://partybooking.herokuapp.com/client/payment/mobile/$it"
                mBinding.root.findNavController().navigate(PaymentPartyFragmentDirections.actionPaymentPartyFragmentToMenuFragment())
                openNewTabWindow(url, requireContext())
            }
        })
    }

    private fun setupRecyclerView(dishes: List<Dishes>) {
        mAdapter.list.addAll(dishes)
        mBinding.rvListDishes.adapter = mAdapter
        mBinding.rvListDishes.setHasFixedSize(true)
    }

    private fun openNewTabWindow(urls: String, context: Context) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        context.startActivity(intents)
    }
}