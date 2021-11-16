package com.keremturker.behero.ui.fragment.login

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentLoginBinding
import com.keremturker.behero.utils.extension.makeClickableText

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginVM>() {
    override val viewModel: LoginVM by viewModels()

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        binding.txtTitle.makeClickableText(fullText = getString(R.string.logo_title_full_text),multiColorArray = arrayOf(getString(R.string.logo_title_part1),getString(R.string.logo_title_part2)))

        binding.txtRegister.makeClickableText(getString(R.string.register_title_full_text),clickableTextArray = arrayOf(getString(R.string.register_title_clickable_text)),functionArray = arrayOf({viewModel.goToRegister()}),multiColorArray = arrayOf(getString(R.string.register_title_clickable_text)))
    }

}