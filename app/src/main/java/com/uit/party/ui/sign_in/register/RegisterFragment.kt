package com.uit.party.ui.sign_in.register

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.uit.party.R
import com.uit.party.databinding.FragmentRegisterBinding
import com.uit.party.ui.sign_in.SignInActivity
import javax.inject.Inject


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    @Inject
    lateinit var viewModel: RegisterViewModel
    private var cX = 0
    private var cY = 0
    private val myArgs : RegisterFragmentArgs by navArgs()


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity() as SignInActivity).signInComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding(container)
        setupListener()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cX = myArgs.cXFAB
        cY = myArgs.cYFAB
        animateRevealShow()
    }

    private fun setupBinding(container: ViewGroup?) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_register,
            container,
            false
        )
        binding.viewModel = viewModel
    }

    private fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(
            binding.cvAdd,
            cX,
            cY,
            binding.cvAdd.height.toFloat(),
            cY.toFloat()
        )
        mAnimator.duration = 300
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.start()
    }

    private fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(
            binding.cvAdd,
            cX,
            cY,
            binding.cvAdd.height.toFloat(),
            cY.toFloat()
        )
        mAnimator.duration = 300
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                (context as SignInActivity).onBackPressed()
            }
        })
        mAnimator.start()
    }

    private fun setupListener(){
        binding.rlContainer.setOnClickListener { animateRevealClose() }
        binding.etEmail.doOnTextChanged { text, _, _, _ -> viewModel.checkEmailValid(text) }
        binding.etFullName.doOnTextChanged { text, _, _, _ -> viewModel.checkFullNameValid(text) }
        binding.etPhoneNumber.doOnTextChanged { text, _, _, _ -> viewModel.checkPhoneNumberValid(text) }
        binding.etUsername.doOnTextChanged { text, _, _, _ -> viewModel.checkUsernameValid(text) }
        binding.etPassword.doOnTextChanged { text, _, _, _ -> viewModel.checkPasswordValid(text) }
        binding.etRepeatPassword.doOnTextChanged { text, _, _, _ -> viewModel.checkConfirmPasswordValid(text) }
    }
}