package com.keremturker.behero.ui.fragment.donation.search

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSearchDonationsBinding
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.ui.fragment.donation.DonationsListAdapter
import com.keremturker.behero.utils.Constants
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.hideKeyboard
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDonationsFragment : BaseFragment<FragmentSearchDonationsBinding, SearchDonationsVM>() {
    override val viewModel: SearchDonationsVM by viewModels()

    override fun getViewBinding() = FragmentSearchDonationsBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true
    override var toolbarType = ToolbarType.Normal
    private val defaultDonationCount = 10L

    private val donationAdapter = DonationsListAdapter(::onClickAction)

    override fun onFragmentCreated() {
        setView()
        prepareRecyclerView()

        binding.txtClearFilter.setOnClickListener {
            binding.apply {
                clearView()
                btnApply.performClick()
            }
        }

        binding.btnApply.setOnClickListener {
            binding.apply {
                clFilter.visibleIf(false)
                btnSearch.visibleIf(true)
                callFindDonation()
                clearView()
                hideKeyboard()
            }
        }

        binding.btnSearch.setOnClickListener {
            callFindDonation()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDonations( bloodGroup = "", address = "", limit = defaultDonationCount)
    }

    override fun observe() {
        viewModel.donations.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)

                is Response.Success -> {
                    viewModel.loadingDetection.postValue(false)
                    if (response.data.isNotEmpty()) {
                        donationAdapter.replaceData(response.data)
                        binding.txtNoRecords.visibleIf(false)
                        binding.rvDonations.visibleIf(true)
                    } else {
                        donationAdapter.replaceData(arrayListOf())
                        binding.rvDonations.visibleIf(false)
                        binding.txtNoRecords.visibleIf(true)
                    }
                }
                is Response.Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireActivity())
                }
            }
        }
    }

    private fun setView() {
        setNormalToolbar(
            title = getString(R.string.find_donation_title),
            rightIcon = R.drawable.ic_filter
        ) {
            val isShow = binding.clFilter.isVisible
            binding.clFilter.visibleIf(!isShow)
            binding.btnSearch.visibleIf(isShow)

            if (isShow) {
                visibleListControl()
            } else {
                binding.rvDonations.visibleIf(false)
                binding.txtNoRecords.visibleIf(false)
            }
        }

        binding.bloodLayout.bloodGroup.clearCheck()

        binding.bloodLayout.bloodGroup.isUncheckedAllow = true
    }

    private fun prepareRecyclerView() {
        binding.rvDonations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = donationAdapter
        }
    }

    private fun onClickAction(item: Donations) {
        viewModel.goToDetailDonation(item)
    }

    private fun visibleListControl() {
        if (donationAdapter.itemCount > 0) {
            binding.txtNoRecords.visibleIf(false)
            binding.rvDonations.visibleIf(true)
        } else {
            binding.rvDonations.visibleIf(false)
            binding.txtNoRecords.visibleIf(true)
        }
    }

    private fun callFindDonation() {

        val bloodGroup =
            binding.bloodLayout.bloodGroup.checkedRadioButtonText?.toString() ?: Constants.emptyText()

        val address = binding.edtAddress.getText()

        viewModel.getDonations(bloodGroup = bloodGroup, address = address)
    }

    private fun clearView(){
        binding.apply {
            bloodLayout.bloodGroup.clearCheck()
            edtAddress.clearText()
        }
    }

}