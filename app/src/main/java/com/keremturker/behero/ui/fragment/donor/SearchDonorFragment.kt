package com.keremturker.behero.ui.fragment.donor

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentSearchDonorBinding
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
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
        setNormalToolbar(title = getString(R.string.find_donor_title))
        prepareRecyclerView()
        binding.searchLayout.imgFilter.setOnClickListener {
            val isShow = !binding.clFilter.isVisible
            binding.clFilter.visibleIf(isShow)
            binding.rvDonor.visibleIf(!isShow)
            binding.btnSearch.visibleIf(!isShow)
        }

        binding.btnApply.setOnClickListener {
            binding.clFilter.visibleIf(false)
        }
        binding.btnSearch.setOnClickListener {
            viewModel.getDonor()
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
                        /*     binding.txtNoRecords.visibleIf(false)
                             binding.rvDonations.visibleIf(true)*/
                    } else {
                        donorAdapter.replaceData(arrayListOf())
                        /*   binding.rvDonations.visibleIf(false)
                           binding.txtNoRecords.visibleIf(true)*/
                    }
                }
                is Response.Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireContext())
                }
            }
        }

    }


    private fun prepareRecyclerView() {
        binding.rvDonor.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = donorAdapter
        }
    }

    private fun onClickAction(item: Users) {}
}