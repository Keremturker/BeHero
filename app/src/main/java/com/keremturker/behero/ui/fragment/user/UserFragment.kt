package com.keremturker.behero.ui.fragment.user


import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding, UserVM>() {
    override val viewModel: UserVM by viewModels()

    override fun getViewBinding() = FragmentUserBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setToolbar(title = getString(R.string.profile_title), rightIcon = R.drawable.ic_edit) {
            viewModel.navToProfileEdit()
        }

    }

    override fun onReselected() {
        super.onReselected()
    }

}