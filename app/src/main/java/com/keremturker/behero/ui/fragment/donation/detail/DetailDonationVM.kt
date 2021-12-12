package com.keremturker.behero.ui.fragment.donation.detail

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Donations
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailDonationVM @Inject constructor(
    val app: Application
) : BaseViewModel(app) {

    fun goToDonationEdit(donation: Donations) {

    }
}