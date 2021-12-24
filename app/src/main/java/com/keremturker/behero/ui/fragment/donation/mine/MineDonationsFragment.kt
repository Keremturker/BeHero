package com.keremturker.behero.ui.fragment.donation.mine

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentMineDonationsBinding
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineDonationsFragment : BaseFragment<FragmentMineDonationsBinding, MineDonationsVM>() {
    override val viewModel: MineDonationsVM by viewModels()

    override fun getViewBinding() = FragmentMineDonationsBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true
    override var toolbarType = ToolbarType.Normal

    private val donationAdapter = MineDonationsListAdapter(::onClickAction)
    override fun onFragmentCreated() {
        setNormalToolbar(isBackIcon = true, title = getString(R.string.mine_request_title))
        prepareRecyclerView()
        binding.fabCreateDonation.setOnClickListener {
            viewModel.goToCreateDonation()
        }
    }

    override fun observe() {

        viewModel.mineDonations.observe(viewLifecycleOwner) { response ->
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

    private fun prepareRecyclerView() {
        binding.rvDonations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = donationAdapter
        }
    }

    private fun onClickAction(item: Donations) {
        viewModel.goToDetailDonation(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMineDonation()
    }
}