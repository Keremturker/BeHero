package com.keremturker.behero.ui.fragment.register


import android.widget.Toast
import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentRegisterBinding
import com.keremturker.behero.utils.Constants.PERMISSION_LOCATION
import com.keremturker.behero.utils.Constants.permissionLocation
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

        binding.btnRegister.setOnClickListener {}

        binding.txtAddress.setOnClickListener {
            requestPermission(PERMISSION_LOCATION, *permissionLocation)
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
}