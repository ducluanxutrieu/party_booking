package com.uit.party.ui.main.add_new_dish

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uit.party.R
import com.uit.party.databinding.FragmentAddNewDishBinding
import com.uit.party.model.Thumbnail
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.UiUtil
import javax.inject.Inject


class AddNewDishFragment : Fragment() {
    private lateinit var mBinding: FragmentAddNewDishBinding

    @Inject
    lateinit var mViewModel: AddNewDishFragmentViewModel

    private val myArgs: AddNewDishFragmentArgs by navArgs()
    private lateinit var mSpinnerAdapter: SpinnerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).menuComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_dish, container, false)
        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSpinner()
        setupListener()
        getData()
    }

    private fun getData() {
        mViewModel.mDishModel = myArgs.StringDishModel
        mViewModel.mDishType = myArgs.dishType
        mViewModel.mPosition = myArgs.position
        if (mViewModel.mDishModel != null) {
            mViewModel.initData()
            for (i in Thumbnail.values().indices){
                if (Thumbnail.values()[i].dishName == mViewModel.mDishModel?.categories?.get(0) ?: "Main Dish"){
                    val spinnerPosition = mSpinnerAdapter.getPosition(Thumbnail.values()[i])
                    mBinding.spinnerDishType.setSelection(spinnerPosition)
                }
            }
        }
    }

    private fun setupSpinner() {
        mSpinnerAdapter = SpinnerAdapter(requireContext(), R.layout.item_spinner_dish, Thumbnail.values())
        mBinding.spinnerDishType.adapter = mSpinnerAdapter
            mBinding.spinnerDishType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val thumbnail = parent.getItemAtPosition(position) as Thumbnail
                    mViewModel.mTypeText = thumbnail.dishName
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    mViewModel.mTypeText = Thumbnail.Thumbnail1.dishName
                }
            }
    }

    private fun setupListener(){
        mBinding.etTitleDish.doOnTextChanged { text, _, _, _ -> mViewModel.checkTitleValid(text) }
        mBinding.etDescriptionDish.doOnTextChanged { text, _, _, _ -> mViewModel.checkDescriptionValid(text) }
        mBinding.etPrice.doOnTextChanged { text, _, _, _ -> mViewModel.checkPriceValid(text) }

        mViewModel.messageCallback.observe(viewLifecycleOwner, {
            UiUtil.showToast(it.second)
            if (it.first){
                this.findNavController().popBackStack()
            }
        })
    }
}