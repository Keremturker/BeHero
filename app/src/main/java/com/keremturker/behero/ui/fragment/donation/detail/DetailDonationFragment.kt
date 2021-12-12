package com.keremturker.behero.ui.fragment.donation.detail

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentDetailDonationBinding
import com.keremturker.behero.model.Donations
import com.keremturker.behero.utils.Constants.DONATION
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.getAddress
import com.keremturker.behero.utils.extension.getBloodImage
import com.keremturker.behero.utils.extension.getNavigationResultLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailDonationFragment :
    BaseFragment<FragmentDetailDonationBinding, DetailDonationVM>() {
    override val viewModel: DetailDonationVM by viewModels()

    override fun getViewBinding() = FragmentDetailDonationBinding.inflate(layoutInflater)

    private var donation: Donations? = null

    override var toolbarType = ToolbarType.Normal

    @Inject
    lateinit var sharedHelper: SharedHelper

    override fun onFragmentCreated() {
        donation = arguments?.getSerializable("donation") as Donations?

        if (sharedHelper.syncUsers?.uuid == donation?.uuid) {
            setNormalToolbar(
                isBackIcon = true,
                title = getString(R.string.donation_detail_title),
                rightIcon = R.drawable.ic_edit
            ) {
                donation?.let {
                    viewModel.goToDonationEdit(it)
                }
            }
        } else {
            setNormalToolbar(isBackIcon = true, title = getString(R.string.donation_detail_title))
        }

    }

    private fun setView(donation: Donations?) {
        donation?.let {
            binding.edtPatientName.setText(it.patientName)
            binding.edtHospitalName.setText(it.hospitalName)
            binding.imgBloodGroup.setBackgroundResource(donation.bloodGroup.getBloodImage())
            binding.edtDescription.setText(donation.description)
            binding.edtAddress.setText(donation.address.getAddress())
        }
    }

    override fun onResume() {
        super.onResume()
        val newDonation = this.getNavigationResultLiveData<Donations>(DONATION)
        newDonation?.value?.let {
            donation = it
            setView(it)
        } ?: setView(donation)
    }
}