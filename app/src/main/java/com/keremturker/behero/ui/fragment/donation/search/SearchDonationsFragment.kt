package com.keremturker.behero.ui.fragment.donation.search

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSearchDonationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDonationsFragment : BaseFragment<FragmentSearchDonationsBinding, SearchDonationsVM>() {
    override val viewModel: SearchDonationsVM by viewModels()

    override fun getViewBinding() = FragmentSearchDonationsBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true

    override fun onFragmentCreated() {}

}