package com.keremturker.behero.ui.fragment.login

import androidx.fragment.app.viewModels
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentLoginBinding
import com.keremturker.behero.utils.extension.makeClickableText

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginVM>() {
    override val viewModel: LoginVM by viewModels()

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        binding.txtTitle.makeClickableText(fullText = "Dare To Donate",multiColorArray = arrayOf("Dare","Donate"))

        binding.txtRegister.makeClickableText("Don’t have an account? Register Now.",multiColorArray = arrayOf("Register Now"))
    }

}