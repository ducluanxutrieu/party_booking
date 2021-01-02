package com.uit.party.ui.profile.profile_fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.uit.party.R
import com.uit.party.databinding.FragmentProfileBinding
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.UiUtil
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileFragment : Fragment(){
    private lateinit var binding : FragmentProfileBinding

    @Inject
    lateinit var mViewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).profileComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile , container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = mViewModel
        binding.cvAvatar.setOnClickListener{
            avatarClicked()
        }
    }

    private fun avatarClicked(){
        PickImageDialog.build(PickSetup()).setOnPickResult { result ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            result.bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            mViewModel.uploadAvatar(result.path) {
                UiUtil.showToast(it)
            }
        }.setOnPickCancel {}
            .show(requireActivity().supportFragmentManager)
    }
}