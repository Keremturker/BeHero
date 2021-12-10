package com.keremturker.behero.ui.fragment.donation.mine

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentMineDonationsBinding
import com.keremturker.behero.utils.ToolbarType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineDonationsFragment : BaseFragment<FragmentMineDonationsBinding, MineDonationsVM>() {
    override val viewModel: MineDonationsVM by viewModels()

    override fun getViewBinding() = FragmentMineDonationsBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true
    override var toolbarType=ToolbarType.Normal
    override fun onFragmentCreated() {
        setNormalToolbar(title = getString(R.string.mine_request_title))
        binding.fabCreateDonation.setOnClickListener {
            viewModel.goToCreateDonation()
        }
    }
}