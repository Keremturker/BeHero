package com.keremturker.behero.ui.fragment.donation.create

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentCreateDonationBinding
import com.keremturker.behero.utils.ToolbarType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDonationFragment : BaseFragment<FragmentCreateDonationBinding, CreateDonationVM>() {
    override val viewModel: CreateDonationVM by viewModels()

    override fun getViewBinding() = FragmentCreateDonationBinding.inflate(layoutInflater)

    override var toolbarType = ToolbarType.Normal
    override fun onFragmentCreated() {
        setNormalToolbar(isBackIcon = true, title = getString(R.string.create_a_request_title))
    }
}