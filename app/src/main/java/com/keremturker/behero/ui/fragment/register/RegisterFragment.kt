package com.keremturker.behero.ui.fragment.register


import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentRegisterBinding
import com.keremturker.behero.model.Response.*
import com.keremturker.behero.model.Users
import com.keremturker.behero.utils.Constants.ADDRESS
import com.keremturker.behero.utils.Constants.PERMISSION_LOCATION
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.Constants.permissionLocation
import com.keremturker.behero.utils.extension.getNavigationResult
import com.keremturker.behero.utils.extension.makeClickableText
import com.keremturker.behero.utils.showDatePicker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterVM>() {

    override val viewModel: RegisterVM by viewModels()
    override fun getViewBinding() = FragmentRegisterBinding.inflate(layoutInflater)

    var birthDay = ""

    @Inject
    lateinit var auth: FirebaseAuth

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

        //  binding.bloodLayout.bloodGroup.setOnCheckedChangeListener(MultiLineRadioGroup.OnCheckedChangeListener { group, button -> })

        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.getText()
            val mail = binding.edtMail.getText()
            val passWord = binding.clPassword.edtPassword.text.toString()
            val birthDay = binding.txtBirthday.text.toString()
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
            }
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
                }
            }
        }

        viewModel.createUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Loading -> {
                }
                is Success -> {
                    viewModel.loadingDetection.postValue(false)
                    clearView()
                    auth.signOut()
                }
                is Failure -> {
                    viewModel.loadingDetection.postValue(false)
                }
            }
        }
    }

    override fun onPermissionGranted(permissions: Array<String>) {
        viewModel.goToMaps()
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
        val address = this.getNavigationResult(ADDRESS)
        address?.value?.let {
            binding.txtAddress.text = it
        }
        if (birthDay != "") {
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
        val address =
            if (binding.txtAddress.text.toString() == requireContext().getString(R.string.address_hint_text)) {
                ""
            } else {
                binding.txtAddress.text.toString()
            }

        return Users(
            uuid = uUid,
            name = name,
            mail = mail,
            birthDay = birthDay,
            gender = gender,
            bloodGroup = bloodGroup,
            address = address,
            phone = phone,
            timestamp = FieldValue.serverTimestamp()
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