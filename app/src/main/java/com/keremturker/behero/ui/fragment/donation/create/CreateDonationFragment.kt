package com.keremturker.behero.ui.fragment.donation.create

import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FieldValue
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentCreateDonationBinding
import com.keremturker.behero.model.Address
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.utils.Constants
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.getNavigationResultLiveData
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateDonationFragment : BaseFragment<FragmentCreateDonationBinding, CreateDonationVM>() {
    override val viewModel: CreateDonationVM by viewModels()

    override fun getViewBinding() = FragmentCreateDonationBinding.inflate(layoutInflater)
    override var toolbarType = ToolbarType.Normal
    var selectedAddress = Address()

    @Inject
    lateinit var sharedHelper: SharedHelper
    override fun onFragmentCreated() {
        setNormalToolbar(isBackIcon = true, title = getString(R.string.create_a_request_title))


        binding.btnRequest.setOnClickListener {
            binding.apply {
                edtPhone.apply { showError(getText().isEmpty()) }
                addressLine.visibleIf(txtAddress.text == getText(R.string.address_hint_text))
            }
            viewModel.createDonation(createDonation())
        }

        binding.txtAddress.setOnClickListener {
            binding.addressLine.visibleIf(false)
            viewModel.goToMaps(selectedAddress.latitude, selectedAddress.longitude)
        }
    }

    override fun observe() {
        viewModel.createDonation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)
                is Response.Success -> {
                    viewModel.loadingDetection.postValue(false)
                    getString(R.string.donation_request_text).showAsDialog(requireContext()) {
                        viewModel.goToBack()
                    }
                }
                is Response.Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireContext())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val address = this.getNavigationResultLiveData<Address>(Constants.ADDRESS)

        address?.value?.let {
            selectedAddress = it
            binding.txtAddress.text = it.description
        }
    }


    private fun createDonation(): Donations {
        val phone = binding.edtPhone.getText()
        val bloodGroup = binding.bloodLayout.bloodGroup.checkedRadioButtonText.toString()
        val description = binding.edtDescription.getText()
        val address = binding.txtAddress.text.toString()

        return Donations(
            uuid = sharedHelper.syncUsers?.uuid ?: "",
            phone = phone,
            bloodGroup = bloodGroup,
            address = address,
            shortAddress = selectedAddress.shortAddress,
            latitude = selectedAddress.latitude,
            longitude = selectedAddress.longitude,
            description = description,
            createTime = FieldValue.serverTimestamp(),
            updateTime = FieldValue.serverTimestamp()
        )
    }

}