package com.keremturker.behero.ui.fragment.user


import androidx.fragment.app.viewModels
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentUserBinding
import com.keremturker.behero.utils.SharedHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding, UserVM>() {
    @Inject
    lateinit var sharedHelper: SharedHelper
    override val viewModel: UserVM by viewModels()

    override fun getViewBinding() = FragmentUserBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setToolbar(title = getString(R.string.profile_title), rightIcon = R.drawable.ic_edit) {
            viewModel.navToProfileEdit()
        }
        setView()

        binding.clSignOut.setOnClickListener {
            viewModel.signOut()
        }

    }

    private fun setView() {
        val user = sharedHelper.syncUsers

        user?.let {
            binding.apply {
                txtName.text = it.name
                txtShortAddress.text = it.shortAddress
            }
        }

        binding.layoutBloodType.txtTitle.text = getString(R.string.blood_type)
        binding.layoutBloodType.txtContent.text = user?.bloodGroup

        binding.layoutRequest.txtTitle.text = getString(R.string.requested)
        binding.layoutRequest.txtContent.text = "200"

    }

    override fun onReselected() {
        super.onReselected()
        setView()
    }

}