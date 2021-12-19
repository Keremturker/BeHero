package com.keremturker.behero.ui.fragment.donor

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


    override fun onFragmentCreated() {

        setView()
        prepareRecyclerView()

        binding.txtClearFilter.setOnClickListener {
            binding.bloodLayout.bloodGroup.clearCheck()
            binding.genderLayout.genderGroup.clearCheck()
            binding.edtAddress.clearText()
        }

        binding.btnApply.setOnClickListener {
            binding.clFilter.visibleIf(false)
            binding.btnSearch.visibleIf(true)
            callFindDonor()
        }

        binding.btnSearch.setOnClickListener {
            callFindDonor()
        }
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
                    response.errorMessage.showAsDialog(requireContext())
                }
            }
        }

    }

    private fun setView() {
        setNormalToolbar(
            title = getString(R.string.find_donor_title),
            rightIcon = R.drawable.ic_baseline_filter_list_32
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

    private fun onClickAction(item: Users) {}

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
}