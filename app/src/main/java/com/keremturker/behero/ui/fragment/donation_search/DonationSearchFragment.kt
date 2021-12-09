package com.keremturker.behero.ui.fragment.donation_search

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentDonationSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonationSearchFragment : BaseFragment<FragmentDonationSearchBinding, DonationSearchVM>() {
    override val viewModel: DonationSearchVM by viewModels()

    override fun getViewBinding() = FragmentDonationSearchBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true

    override fun onFragmentCreated() {}

}