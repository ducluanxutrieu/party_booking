package com.uit.party.ui.main.detail_dish

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.uit.party.R
import com.uit.party.databinding.FragmentDetailDishBinding
import com.uit.party.model.Account
import com.uit.party.model.UserRole
import com.uit.party.ui.main.MainActivity
import com.uit.party.util.Constants.Companion.USER_INFO_KEY
import com.uit.party.util.GlobalApplication
import com.uit.party.util.SharedPrefs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailDishFragment : Fragment() {

    @Inject
    lateinit var viewModel: DetailDishViewModel
    private lateinit var binding: FragmentDetailDishBinding
    private val mArgs: DetailDishFragmentArgs by navArgs()
    private val adapter = DishRatingAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).menuComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding(container)
        return binding.root
    }

    private fun setupBinding(container: ViewGroup?) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_detail_dish,
            container,
            false
        )

        binding.viewModel = viewModel

        setupToolbar()
        ratingClicked()
    }

    private fun ratingClicked(){
        binding.floatingActionButton.setOnClickListener {
            val onRatingDialogListener = object : OnRatingDialogListener {
                override fun onSubmitted(content: String, score: Float) {
                    viewModel.onSubmitClicked(content, score)
                }
            }
            val dialog = RatingDialog(onRatingDialogListener)
            dialog.showsDialog = true
            val frameManager = childFragmentManager
            dialog.show(frameManager, "Dialog Review")
        }
    }

    private fun checkIsStaff(): Boolean {
        val role =
            SharedPrefs(GlobalApplication.appContext!!).getData(USER_INFO_KEY, Account::class.java)?.role
        return (role == UserRole.Admin.ordinal || role == UserRole.Staff.ordinal)
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        val toolbar = activity?.findViewById<View>(R.id.app_bar) as Toolbar

        toolbar.inflateMenu(R.menu.menu_dish_detail)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_edit_dish -> {
                    val action =
                        DetailDishFragmentDirections.actionDishDetailFragmentToModifyDishFragment(
                            position = viewModel.mPosition,
                            dishType = viewModel.mDishType,
                            StringDishModel = viewModel.mDishModel
                        )
                    this.findNavController()
                        .navigate(action)
                    true
                }
                R.id.toolbar_delete_dish -> {
                    deleteDish()
                    true
                }

                R.id.toolbar_add_to_cart -> {
                    viewModel.addToCart()
                    startAnimationAddToCard()
                    true
                }
                else -> false
            }
        }
    }

    private fun deleteDish() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(context?.resources?.let { ResourcesCompat.getDrawable(it, R.drawable.ic_alert, context?.theme) })
            .setTitle(getString(R.string.delete_dish))
            .setMessage(getString(R.string.alert_delete_dish))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                viewModel.deleteDish(binding.root, dialog)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dish_detail, menu)
        if (!checkIsStaff()) {
            menu.findItem(R.id.toolbar_edit_dish).isVisible = false
            menu.findItem(R.id.toolbar_delete_dish).isVisible = false
        }else{
            menu.findItem(R.id.toolbar_add_to_cart).isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        listenLiveData()
        if (viewModel.mDishModel != null) {
            viewModel.init()
        }
        setupRecyclerView()
    }

    private fun startAnimationAddToCard() {
        binding.lavAddToCart.visibility = View.VISIBLE
        binding.lavAddToCart.playAnimation()
        binding.lavAddToCart.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                binding.lavAddToCart.cancelAnimation()
                binding.lavAddToCart.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    private fun setupRecyclerView() {
        binding.imageSlider.sliderAdapter = viewModel.mAdapter
        binding.imageSlider.startAutoCycle()
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

        binding.rvRating.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.rvRating.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initData() {
        viewModel.mPosition = mArgs.position
        viewModel.mDishType = mArgs.dishType
        viewModel.mDishModel = mArgs.StringDishModel
//        viewModel.getListRating()
    }

    private fun listenLiveData(){
        // Make sure we cancel the previous job before creating a new one
        lifecycleScope.launch {
            viewModel.getListRating(dishId = viewModel.mDishModel?.id ?: "").collectLatest {
                adapter.submitData(it)
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvRating.scrollToPosition(0) }
        }

        binding.retryButton.setOnClickListener { adapter.retry() }
    }
}