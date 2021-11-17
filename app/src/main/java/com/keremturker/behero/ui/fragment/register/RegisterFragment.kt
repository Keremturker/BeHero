package com.keremturker.behero.ui.fragment.register


import android.util.Log
import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentRegisterBinding
import com.keremturker.behero.utils.extension.makeClickableText


class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterVM>() {
    override val viewModel: RegisterVM by viewModels()

    override fun getViewBinding() = FragmentRegisterBinding.inflate(layoutInflater)

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

            val text = binding.bloodLayout.bloodGroup.checkedRadioButtonText

            Log.d("test123", text.toString())
        }
    }

}