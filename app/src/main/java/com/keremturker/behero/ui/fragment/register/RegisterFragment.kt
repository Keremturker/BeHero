package com.keremturker.behero.ui.fragment.register


import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FieldValue
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentRegisterBinding
import com.keremturker.behero.model.Address
import com.keremturker.behero.model.Response.*
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.Constants.ADDRESS
import com.keremturker.behero.utils.Constants.PERMISSION_LOCATION
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.Constants.permissionLocation
import com.keremturker.behero.utils.extension.getNavigationResultLiveData
import com.keremturker.behero.utils.extension.isValidEmail
import com.keremturker.behero.utils.extension.makeClickableText
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import com.keremturker.behero.utils.showDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterVM>() {

    override val viewModel: RegisterVM by viewModels()
    override fun getViewBinding() = FragmentRegisterBinding.inflate(layoutInflater)

    private var birthDay = emptyText()
    var selectedAddress = Address()
    override fun onFragmentCreated() {
        binding.txtTitle.makeClickableText(
            fullText = getString(R.string.logo_title_full_text), multiColorArray = arrayOf(
                getString(
                    R.string.logo_title_part1
                ), getString(R.string.logo_title_part2)
            )
        )
        binding.txtLogin.makeClickableText(
            getString(R.string.login_title_full_text),
            clickableTextArray = arrayOf(getString(R.string.login_title_clickable_text)),
            functionArray = arrayOf({ viewModel.goToLogin() }),
            multiColorArray = arrayOf(getString(R.string.login_title_clickable_text))
        )

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.getText()
            val mail = binding.edtMail.getText()
            val passWord = binding.clPassword.edtPassword.text.toString()
            val birthDay = binding.txtBirthday.text.toString()

            binding.apply {
                edtName.apply { showError(getText().isEmpty()) }
                edtMail.apply { showError(!getText().isValidEmail()) }
                clPassword.apply { underLine.visibleIf(edtPassword.text.toString().isEmpty()) }
                birthDayLine.visibleIf(txtBirthday.text == getText(R.string.birthday_hint_text))
            }
            viewModel.signUpWithMail(
                name = name,
                mail = mail,
                passWord = passWord,
                birthDay = birthDay
            )
        }

        binding.txtAddress.setOnClickListener {
            requestPermission(PERMISSION_LOCATION, *permissionLocation)
        }

        binding.txtBirthday.setOnClickListener {
            requireContext().showDatePicker(binding.txtBirthday.text.toString()) {
                binding.txtBirthday.text = it
                birthDay = it
                binding.birthDayLine.visibleIf(false)
            }
        }
        binding.clPassword.edtPassword.addTextChangedListener {
            binding.clPassword.underLine.visibleIf(false)
        }
    }

    override fun observe() {
        viewModel.signUpUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Loading -> viewModel.loadingDetection.postValue(true)
                is Success -> {
                    viewModel.createUserInFirestore(createUserDate(response.data.uid))
                }
                is Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireActivity())
                }
            }
        }

        viewModel.createUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Loading -> {
                }
                is Success -> {
                    viewModel.sendActivationMail()
                }
                is Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireActivity())
                }
            }
        }
        viewModel.activationMail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Loading -> {
                }
                is Success -> {
                    viewModel.loadingDetection.postValue(false)
                    clearView()
                    getString(R.string.account_activation).showAsDialog(requireActivity()) {
                        viewModel.goToLogin()
                    }
                }
                is Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireActivity())
                }
            }
        }
    }

    override fun onPermissionGranted(permissions: Array<String>) {
        viewModel.goToMaps(selectedAddress.latitude ?: 0.0, selectedAddress.longitude ?: 0.0)

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
        val address = this.getNavigationResultLiveData<Address>(ADDRESS)

        address?.value?.let {
            selectedAddress = it
            binding.txtAddress.text = it.description
        }
        if (birthDay != emptyText()) {
            binding.txtBirthday.text = birthDay

        }
    }

    private fun createUserDate(uUid: String): Users {

        val name = binding.edtName.getText()
        val mail = binding.edtMail.getText()
        val phone = binding.edtPhone.getText()
        val birthDay = binding.txtBirthday.text.toString()
        val gender = binding.genderLayout.genderGroup.checkedRadioButtonText.toString()
        val bloodGroup = binding.bloodLayout.bloodGroup.checkedRadioButtonText.toString()

        return Users(
            uuid = uUid,
            name = name,
            mail = mail,
            birthDay = birthDay,
            gender = gender,
            bloodGroup = bloodGroup,
            address = selectedAddress,
            phone = phone,
            createTime = FieldValue.serverTimestamp(),
            updateTime = FieldValue.serverTimestamp(),
            availableDonate = true,
            mailVerified = false
        )
    }

    private fun clearView() {
        binding.apply {
            edtName.clearText()
            edtMail.clearText()
            clPassword.edtPassword.setText(emptyText())
            edtPhone.clearText()
            txtBirthday.text = getString(R.string.birthday_hint_text)
            genderLayout.genderGroup.checkAt(0)
            bloodLayout.bloodGroup.checkAt(0)
            txtAddress.text = getString(R.string.address_hint_text)
        }
    }
}