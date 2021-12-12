package com.keremturker.behero.ui.fragment.donation.detail

import android.app.Application
import android.os.Bundle
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Donations
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class DetailDonationVM @Inject constructor(
    val app: Application
) : BaseViewModel(app) {

    fun goToDonationEdit(donation: Donations) {
        Bundle().apply {
            putSerializable("donation", donation as Serializable)
            navigateFragment(
                navAction = R.id.nav_action_createUpdateDonationFragment_global,
                bundle = this
            )
        }
    }
}