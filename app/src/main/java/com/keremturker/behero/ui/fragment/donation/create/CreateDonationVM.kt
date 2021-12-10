package com.keremturker.behero.ui.fragment.donation.create

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.repository.DonationRepository
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateDonationVM @Inject constructor(
    val app: Application,
    private val donationRepository: DonationRepository
) : BaseViewModel(app) {

    private val _createDonation = SingleLiveEvent<Response<Void>>()
    val createDonation: LiveData<Response<Void>> = _createDonation

    fun goToMaps(latitude: Double, longitude: Double) {
        Bundle().apply {
            putDouble("latitude", latitude)
            putDouble("longitude", longitude)
            navigateFragment(navAction = R.id.nav_action_mapsFragment_global, bundle = this)
        }
    }

    fun goToBack() {
        navigateFragment(navAction = R.id.nav_action_createDonationFragment_global_remove)
    }

    fun createDonation(donation: Donations) {
        if (validationParameters(donation)) {
            viewModelScope.launch {
                donationRepository.createDonationInFirestore(donation).collect {
                    _createDonation.postValue(it)
                }
            }
        } else {
            _createDonation.postValue(Response.Failure(app.getString(R.string.required_filed_text)))
        }

    }

    private fun validationParameters(donation: Donations): Boolean {

        return donation.address != app.getString(R.string.address_hint_text) && donation.phone.isNotEmpty() && donation.bloodGroup.isNotEmpty()
    }
}