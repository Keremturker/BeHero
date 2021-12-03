package com.keremturker.behero.ui.fragment.user.profile_edit

import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentProfileEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding, ProfileEditVM>() {
    override val viewModel: ProfileEditVM by viewModels()

    override fun getViewBinding() = FragmentProfileEditBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setToolbar(isBackIcon = true, getString(R.string.profile_edit_title))
    }
}