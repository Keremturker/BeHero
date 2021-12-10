package com.keremturker.behero.ui.fragment.donation.mine

import android.app.Application
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MineDonationsVM @Inject constructor(app: Application) : BaseViewModel(app){

    fun goToCreateDonation(){
        navigateFragment(navAction = R.id.nav_action_createDonationFragment_global)
    }
}