package com.uit.party.ui.profile.change_password

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uit.party.R
import com.uit.party.databinding.FragmentChangePasswordBinding
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.UiUtil
import javax.inject.Inject

class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding

    @Inject
    lateinit var mViewModel: ChangePasswordViewModel
    private val myArgs: ChangePasswordFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity() as MainActivity).profileComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        binding.viewModel = mViewModel
        setupListener()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.init(myArgs.OrderCode)
    }

    private fun setupListener() {
        binding.etCurrentPassword.doOnTextChanged { text, _, _, _ ->
            mViewModel.checkCurrentPasswordValid(
                text
            )
        }
        binding.etNewPassword.doOnTextChanged { text, _, _, _ ->
            mViewModel.checkNewPasswordValid(
                text
            )
        }
        binding.etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            mViewModel.checkConfirmPasswordValid(
                text
            )
        }

        mViewModel.messageCallback.observe(viewLifecycleOwner, {
            UiUtil.showToast(it.second)
            if (it.first) {
                if (mViewModel.mOrderCode == "CHANGE")
                    this.findNavController().popBackStack()
                else
                    this.findNavController()
                        .navigate(R.id.action_ResetPasswordFragment_back_LoginFragment)
            }
        })
    }
}
