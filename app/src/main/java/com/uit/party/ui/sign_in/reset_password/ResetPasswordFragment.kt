package com.uit.party.ui.sign_in.reset_password

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
import com.uit.party.databinding.FragmentResetPasswordBinding
import com.uit.party.ui.sign_in.SignInActivity
import com.uit.party.util.UiUtil
import com.uit.party.util.usernameErrorMes
import javax.inject.Inject

class ResetPasswordFragment : Fragment() {

    @Inject
    lateinit var viewModel: ResetPasswordViewModel

    private lateinit var binding: FragmentResetPasswordBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity = requireActivity()
        if (activity is SignInActivity){
            activity.signInComponent.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        binding.viewModel = viewModel
        setupListener()
        return binding.root
    }

    private fun setupListener(){
        binding.etUsername.doOnTextChanged { text, _, _, _ ->
             val result = text.usernameErrorMes()
            viewModel.errorUsername.set(result)
            binding.btGo.isEnabled = result.isEmpty()
        }

        binding.btGo.setOnClickListener {
            viewModel.requestResetPassword(binding.etUsername.text.toString())
        }

        viewModel.resetPassState.observe(viewLifecycleOwner, {
            UiUtil.showToast(it.second)
            if (it.first){
                val action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToChangePasswordFragment("RESET")
                this.findNavController().navigate(action)
            }
        })
    }
}