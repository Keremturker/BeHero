package com.keremturker.behero.ui.fragment.user.profile_edit

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentProfileEditBinding
import com.keremturker.behero.utils.SharedHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, ProfileEditVM>() {
    override val viewModel: ProfileEditVM by viewModels()

    override fun getViewBinding() = FragmentProfileEditBinding.inflate(layoutInflater)

    @Inject
    lateinit var sharedHelper: SharedHelper
    override fun onFragmentCreated() {
        setToolbar(isBackIcon = true, getString(R.string.profile_edit_title))
        setView()
    }

    private fun setView() {
        sharedHelper.syncUsers?.let {
            binding.apply {
                edtName.setText(it.name)
                edtMail.setText(it.mail)
                edtPhone.setText(it.phone)
                txtBirthday.text = it.birthDay
                genderLayout.genderGroup.check(it.gender)
                bloodLayout.bloodGroup.check(it.bloodGroup)
                txtAddress.text=it.address
            }
        }
    }
}