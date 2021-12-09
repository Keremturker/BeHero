package com.keremturker.behero.ui.fragment.user.profile_edit

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FieldValue
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentProfileEditBinding
import com.keremturker.behero.model.Address
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.*
import com.keremturker.behero.utils.extension.getNavigationResultLiveData
import com.keremturker.behero.utils.extension.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, ProfileEditVM>() {
    override val viewModel: ProfileEditVM by viewModels()

    override fun getViewBinding() = FragmentProfileEditBinding.inflate(layoutInflater)
    var birthDay = ""
    lateinit var selectedAddress: Address
    override var toolbarType: ToolbarType = ToolbarType.Normal

    @Inject
    lateinit var sharedHelper: SharedHelper
    override fun onFragmentCreated() {
        setNormalToolbar(isBackIcon = true, getString(R.string.profile_edit_title))
        setView()
    }

    override fun observe() {
        super.observe()
        viewModel.updateUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)
                is Response.Success -> {

                    viewModel.loadingDetection.postValue(false)
                    getString(R.string.update_information).showAsDialog(requireContext()) {
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
        sharedHelper.syncUsers?.let {
            binding.apply {
                selectedAddress = Address(it.address, it.latitude, it.longitude, it.shortAddress)
                edtName.setText(it.name)
                edtMail.setText(it.mail)
                edtPhone.setText(it.phone)
                txtBirthday.text = it.birthDay
                genderLayout.genderGroup.check(it.gender)
                bloodLayout.bloodGroup.check(it.bloodGroup)
                txtAddress.text = it.address

                btnUpdate.setOnClickListener { button ->
                    binding.apply {
                        edtName.apply { showError(getText().isEmpty()) }
                        birthDayLine.visibleIf(txtBirthday.text == getText(R.string.birthday_hint_text))
                    }

                    val user = createUserDate(it)
                    viewModel.updateUser(user)

                }

                txtAddress.setOnClickListener {
                    requestPermission(Constants.PERMISSION_LOCATION, *Constants.permissionLocation)
                }

                txtBirthday.setOnClickListener {
                    requireContext().showDatePicker(binding.txtBirthday.text.toString()) {
                        binding.txtBirthday.text = it
                        birthDay = it
                        binding.birthDayLine.visibleIf(false)
                    }
                }
            }
        }

    }


    private fun createUserDate(users: Users): Users {

        val name = binding.edtName.getText()
        val phone = binding.edtPhone.getText()
        val birthDay = binding.txtBirthday.text.toString()
        val gender = binding.genderLayout.genderGroup.checkedRadioButtonText.toString()
        val bloodGroup = binding.bloodLayout.bloodGroup.checkedRadioButtonText.toString()


        return Users(
            uuid = users.uuid,
            name = name,
            mail = users.mail,
            birthDay = birthDay,
            gender = gender,
            bloodGroup = bloodGroup,
            address = selectedAddress.description,
            shortAddress = selectedAddress.shortAddress,
            phone = phone,
            createTime = users.createTime,
            updateTime = FieldValue.serverTimestamp(),
            latitude = selectedAddress.latitude,
            longitude = selectedAddress.longitude,
            availableDonate = users.availableDonate
        )
    }

    override fun onPermissionGranted(permissions: Array<String>) {
        viewModel.goToMaps(selectedAddress.latitude, selectedAddress.longitude)
    }

    override fun onPermissionDenied(permissions: Array<String>) {
        val showRationale = shouldShowRequestPermissionRationale(permissions[0])
        //check never ask again
        if (!showRationale) {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onResume() {
        super.onResume()

        val address = this.getNavigationResultLiveData<Address>(Constants.ADDRESS)

        address?.value?.let {
            selectedAddress = it
            binding.txtAddress.text = it.description
        }

        if (birthDay != "") {
            binding.txtBirthday.text = birthDay
        }
    }
}