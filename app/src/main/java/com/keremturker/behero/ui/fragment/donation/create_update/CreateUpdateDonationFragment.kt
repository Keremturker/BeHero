package com.keremturker.behero.ui.fragment.donation.create_update

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentCreateUpdateDonationBinding
import com.keremturker.behero.model.Address
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.utils.Constants
import com.keremturker.behero.utils.Constants.DONATION
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.getNavigationResultLiveData
import com.keremturker.behero.utils.extension.setNavigationResult
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateUpdateDonationFragment :
    BaseFragment<FragmentCreateUpdateDonationBinding, CreateUpdateDonationVM>() {
    override val viewModel: CreateUpdateDonationVM by viewModels()

    override fun getViewBinding() = FragmentCreateUpdateDonationBinding.inflate(layoutInflater)
    override var toolbarType = ToolbarType.Normal
    var selectedAddress = Address()

    private val donation: Donations? get() = arguments?.getSerializable("donation") as Donations?
    var newDonation = Donations()

    @Inject
    lateinit var sharedHelper: SharedHelper

    override fun onFragmentCreated() {
        val title = donation?.let {
            getString(R.string.update_request_title)
        } ?: getString(R.string.create_a_request_title)

        setNormalToolbar(isBackIcon = true, title = title)

        binding.btnCreateUpdate.setOnClickListener {
            binding.apply {
                edtPhone.apply { showError(getText().isEmpty()) }
                edtHospitalName.apply { showError(getText().isEmpty()) }
                edtPatientName.apply { showError(getText().isEmpty()) }
                edtPhone.apply { showError(getText().isEmpty()) }
                addressLine.visibleIf(txtAddress.text == getText(R.string.address_hint_text) || txtAddress.text.isEmpty())
            }
            donation?.let {
                newDonation = donation()
                viewModel.updateDonation(newDonation)
            } ?: viewModel.createDonation(donation())
        }

        binding.txtAddress.setOnClickListener {
            binding.addressLine.visibleIf(false)
            viewModel.goToMaps(selectedAddress.latitude ?: 0.0, selectedAddress.longitude ?: 0.0)
        }

        setView(donation)
    }

    private fun setView(donation: Donations?) {
        donation?.let {
            binding.apply {
                edtPatientName.setText(it.patientName)
                edtHospitalName.setText(it.hospitalName)
                selectedAddress = it.address
                txtAddress.text = it.address.description
                bloodLayout.bloodGroup.check(it.bloodGroup)
                edtPhone.setText(it.phone)
                edtDescription.setText(it.description)
                btnCreateUpdate.text = getString(R.string.update_button_text)
            }
        }
    }

    override fun observe() {
        viewModel.createDonation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)
                is Response.Success -> {
                    viewModel.loadingDetection.postValue(false)
                    getString(R.string.donation_created_request_text).showAsDialog(requireContext()) {
                        viewModel.goToBack()
                    }
                }
                is Response.Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireContext())
                }
            }
        }

        viewModel.updateDonation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)
                is Response.Success -> {
                    viewModel.loadingDetection.postValue(false)
                    getString(R.string.donation_updated_request_text).showAsDialog(requireContext()) {
                        this.setNavigationResult(newDonation, DONATION)
                        findNavController().navigateUp()
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


    private fun donation(): Donations {
        val phone = binding.edtPhone.getText()
        val bloodGroup = binding.bloodLayout.bloodGroup.checkedRadioButtonText.toString()
        val description = binding.edtDescription.getText()
        val hospital = binding.edtHospitalName.getText()
        val patient = binding.edtPatientName.getText()
        val createTime = donation?.createTime ?: FieldValue.serverTimestamp()

        return Donations(
            documentId = donation?.documentId ?: emptyText(),
            uuid = sharedHelper.syncUsers?.uuid ?: emptyText(),
            hospitalName = hospital,
            patientName = patient,
            enable = true,
            phone = phone,
            bloodGroup = bloodGroup,
            address = selectedAddress,
            description = description,
            createTime = createTime,
            updateTime = FieldValue.serverTimestamp()
        )
    }
}