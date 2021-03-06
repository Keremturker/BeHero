package com.keremturker.behero.ui.fragment.donor.search

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSearchDonorBinding
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.hideKeyboard
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDonorFragment : BaseFragment<FragmentSearchDonorBinding, SearchDonorVM>() {
    override val viewModel: SearchDonorVM by viewModels()

    override fun getViewBinding() = FragmentSearchDonorBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true

    override var toolbarType = ToolbarType.Normal
    private val donorAdapter = SearchDonorListAdapter(::onClickAction)
    private val defaultUsersCount = 10L

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
                callFindDonor()
                clearView()
                hideKeyboard()
            }
        }

        binding.btnSearch.setOnClickListener {
            callFindDonor()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDonor(gender = "", bloodGroup = "", address = "", limit = defaultUsersCount)
    }


    override fun observe() {

        viewModel.users.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)

                is Response.Success -> {
                    viewModel.loadingDetection.postValue(false)
                    if (response.data.isNotEmpty()) {
                        donorAdapter.replaceData(response.data)
                        binding.txtNoRecords.visibleIf(false)
                        binding.rvDonor.visibleIf(true)
                    } else {
                        donorAdapter.replaceData(arrayListOf())
                        binding.rvDonor.visibleIf(false)
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
            title = getString(R.string.find_donor_title),
            rightIcon = R.drawable.ic_filter
        ) {
            val isShow = binding.clFilter.isVisible
            binding.clFilter.visibleIf(!isShow)
            binding.btnSearch.visibleIf(isShow)

            if (isShow) {
                visibleListControl()
            } else {
                binding.rvDonor.visibleIf(false)
                binding.txtNoRecords.visibleIf(false)
            }
        }

        binding.bloodLayout.bloodGroup.clearCheck()
        binding.genderLayout.genderGroup.clearCheck()

        binding.genderLayout.genderGroup.isUncheckedAllow = true
        binding.bloodLayout.bloodGroup.isUncheckedAllow = true
    }

    private fun prepareRecyclerView() {
        binding.rvDonor.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = donorAdapter
        }
    }

    private fun onClickAction(item: Users) {
        viewModel.goToDetailDonor(item)
    }

    private fun visibleListControl() {
        if (donorAdapter.itemCount > 0) {
            binding.txtNoRecords.visibleIf(false)
            binding.rvDonor.visibleIf(true)
        } else {
            binding.rvDonor.visibleIf(false)
            binding.txtNoRecords.visibleIf(true)
        }
    }

    private fun callFindDonor() {
        val gender =
            binding.genderLayout.genderGroup.checkedRadioButtonText?.toString() ?: emptyText()

        val bloodGroup =
            binding.bloodLayout.bloodGroup.checkedRadioButtonText?.toString() ?: emptyText()

        val address = binding.edtAddress.getText()

        viewModel.getDonor(gender = gender, bloodGroup = bloodGroup, address = address)
    }

    private fun clearView(){
       binding.apply {
           bloodLayout.bloodGroup.clearCheck()
           genderLayout.genderGroup.clearCheck()
           edtAddress.clearText()
       }
    }
}