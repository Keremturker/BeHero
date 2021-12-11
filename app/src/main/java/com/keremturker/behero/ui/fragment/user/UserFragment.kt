package com.keremturker.behero.ui.fragment.user


import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FieldValue
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseFragment
import com.keremturker.behero.databinding.FragmentUserBinding
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.ToolbarType
import com.keremturker.behero.utils.extension.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding, UserVM>() {
    @Inject
    lateinit var sharedHelper: SharedHelper
    override val viewModel: UserVM by viewModels()

    override fun getViewBinding() = FragmentUserBinding.inflate(layoutInflater)
    override var onNavigationViewShow = true
    override var toolbarType: ToolbarType = ToolbarType.Normal


    override fun onFragmentCreated() {
        setNormalToolbar(
            title = getString(R.string.profile_title),
            rightIcon = R.drawable.ic_edit
        ) {
            viewModel.goToProfileEdit()
        }
        setView()

        binding.clSignOut.setOnClickListener {
            viewModel.signOut()
            reloadActivity()
        }
        binding.scDonate.setOnCheckedChangeListener { _, isChecked ->
            sharedHelper.syncUsers?.let {
                it.availableDonate = isChecked
                it.updateTime = FieldValue.serverTimestamp()
                viewModel.setAvailableDonation(it)
            }
        }
        binding.btnRequest.onClickLister {
            viewModel.goToCreateDonation()
        }

    }

    private fun setView() {
        val user = sharedHelper.syncUsers

        user?.let {
            binding.apply {
                txtName.text = it.name

                if (it.address?.cityName?.isEmpty() == true) {
                    llShortAddress.visibleIf(false)
                } else {
                    txtShortAddress.text = it.address?.cityName
                }

                layoutBloodType.txtTitle.text = getString(R.string.blood_type)
                layoutBloodType.txtContent.text = it.bloodGroup
                layoutRequest.txtTitle.text = getString(R.string.requested)
                layoutRequest.txtContent.text = "200"
                scDonate.isChecked = it.availableDonate
            }
        }

    }
}