package com.uit.party.ui.sign_in.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.uit.party.R
import com.uit.party.databinding.FragmentLoginBinding
import com.uit.party.ui.main.MainActivity
import com.uit.party.ui.sign_in.LoginSuccess
import com.uit.party.ui.sign_in.SignInActivity
import javax.inject.Inject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModel: LoginViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity() as SignInActivity).signInComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding(container = container)
        setupListener()
        return binding.root

    }

    private fun setupBinding(container: ViewGroup?) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_login,
            container,
            false
        )
        binding.viewModel = viewModel
    }

    private fun setupListener() {
        binding.etUsername.doOnTextChanged { text, _, _, _ ->
            viewModel.checkUsernameValid(text)
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.checkPasswordValid(text)
        }

        viewModel.loginState.observe(viewLifecycleOwner, {
            if (it is LoginSuccess)
                startMainActivity()
        })
    }

    private fun startMainActivity() {
        val activity = requireActivity()
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}
