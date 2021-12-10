package com.keremturker.behero.ui.fragment.donation.mine

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.repository.DonationRepository
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MineDonationsVM @Inject constructor(
    app: Application,
    private val donationRepository: DonationRepository,
    private val sharedHelper: SharedHelper
) : BaseViewModel(app) {

    private val _mineDonations = SingleLiveEvent<Response<List<Donations>>>()
    val mineDonations: LiveData<Response<List<Donations>>> = _mineDonations


    fun goToCreateDonation() {
        navigateFragment(navAction = R.id.nav_action_createDonationFragment_global)
    }


    fun getMineDonation() {
        viewModelScope.launch {
            sharedHelper.syncUsers?.let {
                donationRepository.getDonationsFromFirestore(it.uuid).collect { response ->
                    _mineDonations.postValue(response)
                }
            }
        }
    }
}