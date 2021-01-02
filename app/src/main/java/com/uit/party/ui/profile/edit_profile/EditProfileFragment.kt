package com.uit.party.ui.profile.edit_profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uit.party.R
import com.uit.party.databinding.FragmentEditProfileBinding
import com.uit.party.model.UserGender
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.UiUtil
import javax.inject.Inject

@Suppress("DEPRECATION")
class EditProfileFragment : Fragment(){
    private lateinit var binding: FragmentEditProfileBinding

    @Inject
    lateinit var mViewModel: EditProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).profileComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRadioButton()
        setupListener()
    }

    private fun setupRadioButton(){
        binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rb_male -> mViewModel.mSex = getString(R.string.sex_male)
                R.id.rb_female -> mViewModel.mSex = getString(R.string.sex_female)
            }
        }

        when (mViewModel.account?.gender){
            UserGender.Male.ordinal -> binding.rbMale.isChecked = true
            UserGender.Female.ordinal -> binding.rbFemale.isChecked = true
        }
    }

    private fun setupListener(){
        binding.etEmail.doOnTextChanged { text, _, _, _ -> mViewModel.checkEmailValid(text) }
        binding.etFullName.doOnTextChanged { text, _, _, _ -> mViewModel.checkFullNameValid(text) }
        binding.etPhoneNumber.doOnTextChanged { text, _, _, _ -> mViewModel.checkPhoneNumberValid(text) }

        mViewModel.messageCallback.observe(viewLifecycleOwner, {
            UiUtil.showToast(it.second)
            if (it.first){
                this.findNavController().popBackStack()
            }
        })
    }
}