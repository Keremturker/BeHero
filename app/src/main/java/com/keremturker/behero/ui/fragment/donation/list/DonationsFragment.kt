package com.keremturker.behero.ui.fragment.donation.list

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.base.SwipeToDeleteCallback
import com.keremturker.behero.databinding.FragmentDonationsBinding
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.ui.fragment.donation.DonationsListAdapter
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DonationsFragment : BaseFragment<FragmentDonationsBinding, DonationsVM>() {
    override val viewModel: DonationsVM by viewModels()

    override fun getViewBinding() = FragmentDonationsBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true
    override var toolbarType = ToolbarType.Normal
    private var user: Users? = null

    private val donationAdapter = DonationsListAdapter(::onClickAction, ::onDeleteAction)

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onFragmentCreated() {
        user = arguments?.getSerializable("users") as Users?

        if (user?.uuid == auth.uid) {
            setNormalToolbar(isBackIcon = true, title = getString(R.string.mine_request_title))
            binding.fabCreateDonation.visibleIf(true)


            binding.fabCreateDonation.setOnClickListener {
                viewModel.goToCreateDonation()
            }

            val swipeDelete = object : SwipeToDeleteCallback(requireContext()) {

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    donationAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }

            val touchHelper = ItemTouchHelper(swipeDelete)
            touchHelper.attachToRecyclerView(binding.rvDonations)

        } else {
            setNormalToolbar(isBackIcon = true, title = user?.name ?: "")
            binding.fabCreateDonation.visibleIf(false)
        }


        prepareRecyclerView()


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

        viewModel.deleteDonation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)

                is Response.Success -> {
                    viewModel.getDonation(user?.uuid)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun onDeleteAction(item: Donations) {
        getString(R.string.are_you_sure_delete_donation).showAsDialog(
            context = requireActivity(),
            cancelButtonState = true,
            cancelFunction = {
                donationAdapter.notifyDataSetChanged()
            },
            okFunction = {
                donationAdapter.notifyDataSetChanged()
                viewModel.deleteDonation(item)
            })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDonation(user?.uuid)
    }
}