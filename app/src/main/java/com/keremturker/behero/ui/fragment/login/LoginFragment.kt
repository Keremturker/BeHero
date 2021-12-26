package com.keremturker.behero.ui.fragment.login

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentLoginBinding
import com.keremturker.behero.model.Response
import com.keremturker.behero.utils.Constants.emptyText
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.extension.isValidEmail
import com.keremturker.behero.utils.extension.makeClickableText
import com.keremturker.behero.utils.extension.visibleIf
import com.keremturker.behero.utils.showAsDialog
import com.keremturker.behero.utils.showMailVerifiedDialog
import com.keremturker.behero.utils.showResetPasswordDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginVM>() {

    @Inject
    lateinit var sharedHelper: SharedHelper
    override val viewModel: LoginVM by viewModels()

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        binding.txtTitle.makeClickableText(
            fullText = getString(R.string.logo_title_full_text),
            multiColorArray = arrayOf(
                getString(R.string.logo_title_part1),
                getString(R.string.logo_title_part2)
            )
        )

        binding.txtRegister.makeClickableText(
            getString(R.string.register_title_full_text),
            clickableTextArray = arrayOf(getString(R.string.register_title_clickable_text)),
            functionArray = arrayOf({ viewModel.goToRegister() }),
            multiColorArray = arrayOf(getString(R.string.register_title_clickable_text))
        )
        binding.btnLogin.setOnClickListener {
            val mail = binding.edtMail.getText()
            val passWord = binding.clPassword.edtPassword.text.toString()

            binding.apply {
                edtMail.apply { showError(!getText().isValidEmail()) }
                clPassword.apply { underLine.visibleIf(edtPassword.text.toString().isEmpty()) }
            }
            viewModel.signInWithMail(mail, passWord)
        }
        binding.clPassword.edtPassword.addTextChangedListener {
            binding.clPassword.underLine.visibleIf(false)
        }

        binding.txtForgetPassword.setOnClickListener {
            requireContext().showResetPasswordDialog { email, dissmis ->

                viewModel.sendResetPassword(email, dissmis)
            }
        }
    }

    override fun observe() {
        viewModel.signInUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> viewModel.loadingDetection.postValue(true)

                is Response.Success -> {
                    if (!response.data.isEmailVerified) {
                        viewModel.loadingDetection.postValue(false)

                        requireContext().showMailVerifiedDialog(response.data) {
                            viewModel.sendMailVerified(response.data)
                        }
                        viewModel.singOut()
                    } else {
                        viewModel.getUser()
                    }
                }
                is Response.Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    response.errorMessage.showAsDialog(requireActivity())
                }
            }
        }

        viewModel.getUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}

                is Response.Success -> {
                    viewModel.loadingDetection.postValue(false)
                    binding.edtMail.clearText()
                    binding.clPassword.edtPassword.setText(emptyText())
                    sharedHelper.syncUsers = response.data
                    response.data.apply {
                        this.mailVerified = true
                        viewModel.createOrUpdateUserInFirestore(this)
                    }
                    viewModel.goToMainScreen()
                }
                is Response.Failure -> {
                    viewModel.loadingDetection.postValue(false)
                    viewModel.singOut()
                    response.errorMessage.showAsDialog(requireActivity())
                }
            }
        }
    }
}