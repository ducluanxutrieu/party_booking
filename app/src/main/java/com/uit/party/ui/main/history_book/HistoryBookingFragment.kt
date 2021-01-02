package com.uit.party.ui.main.history_book

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.uit.party.R
import com.uit.party.databinding.FragmentHistoryBookingBinding
import com.uit.party.ui.main.MainActivity
import com.uit.party.ui.main.detail_dish.ReposLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryBookingFragment : Fragment(){
    private lateinit var mBinding: FragmentHistoryBookingBinding

    @Inject
    lateinit var mViewModel: HistoryBookingViewModel
    private val mAdapter = UserCardAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).orderComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_booking, container, false)

        mBinding.viewModel = mViewModel
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        listenLiveData()
    }

    private fun setupRecyclerView() {
        mBinding.srlListHistory.setOnRefreshListener {
            mAdapter.refresh()
        }

        mBinding.rvListOrdered.setHasFixedSize(true)

        mBinding.rvListOrdered.adapter = mAdapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { mAdapter.retry() },
            footer = ReposLoadStateAdapter { mAdapter.retry() }
        )

        mAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            mBinding.rvListOrdered.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            mBinding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            mBinding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                var errorMessage = it.error.toString()
                if (errorMessage.contains("Unable to resolve host")){
                    errorMessage = "Unable to connect to server"
                }

                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops $errorMessage",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun listenLiveData(){
        lifecycleScope.launch {
            mViewModel.getHistoryOrdered().collectLatest {
                mBinding.srlListHistory.isRefreshing = false
                mAdapter.submitData(it)
            }
        }
    }
}