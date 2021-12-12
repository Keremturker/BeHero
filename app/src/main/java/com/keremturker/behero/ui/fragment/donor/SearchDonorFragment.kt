package com.keremturker.behero.ui.fragment.donor

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSearchDonorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDonorFragment : BaseFragment<FragmentSearchDonorBinding, SearchDonorVM>() {
    override val viewModel: SearchDonorVM by viewModels()

    override fun getViewBinding() = FragmentSearchDonorBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true


    override fun onFragmentCreated() {}
}