package com.keremturker.behero.ui.fragment.main

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentMainBinding
import com.keremturker.behero.model.Response
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.getAddress
import com.keremturker.behero.utils.extension.getBloodImage
import com.keremturker.behero.utils.extension.visibleIf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainMV>() {
    override val viewModel: MainMV by viewModels()

    override fun getViewBinding() = FragmentMainBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true
    override var toolbarType = ToolbarType.Normal

    override fun onFragmentCreated() {
        setNormalToolbar(
            title = getString(R.string.be_hero),
        )
    }

    override fun observe() {
        viewModel.countDonation.observe(this) { response ->
            when (response) {
                is Response.Loading -> binding.pbDonationCount.visibleIf(true)
                is Response.Success -> {
                    binding.pbDonationCount.visibleIf(false)
                    binding.txtDonationContent.text = response.data.toString()
                }
                is Response.Failure -> {
                    binding.pbDonationCount.visibleIf(false)
                    binding.txtDonationContent.text = "0"
                }
            }
        }

        viewModel.countDonor.observe(this) { response ->
            when (response) {
                is Response.Loading -> binding.pbDonorCount.visibleIf(true)
                is Response.Success -> {
                    binding.pbDonorCount.visibleIf(false)
                    binding.txtDonorContent.text = response.data.toString()
                }
                is Response.Failure -> {
                    binding.pbDonorCount.visibleIf(false)
                    binding.txtDonorContent.text = "0"
                }
            }
        }

        viewModel.lastDonation.observe(this) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val donation = response.data[0]
                    binding.cvLastDonation.visibleIf(true)

                    binding.txtName.text = donation.patientName
                    binding.txtLocation.text = donation.address.getAddress()
                    binding.imgBloodGroup.setBackgroundResource(donation.bloodGroup.getBloodImage())

                    binding.cvLastDonation.setOnClickListener {
                        viewModel.goToDetailDonation(donation)
                    }

                }
                is Response.Failure -> {
                    binding.cvLastDonation.visibleIf(false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllDonationCount()
        viewModel.getAllDonorCount()
        viewModel.getLastDonation()
    }
}