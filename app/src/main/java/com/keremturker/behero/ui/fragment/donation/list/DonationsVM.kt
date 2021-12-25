package com.keremturker.behero.ui.fragment.donation.list

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
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class DonationsVM @Inject constructor(
    app: Application,
    private val donationRepository: DonationRepository
) : BaseViewModel(app) {

    private val _donations = SingleLiveEvent<Response<List<Donations>>>()
    val donations: LiveData<Response<List<Donations>>> = _donations

    private val _deleteDonation = SingleLiveEvent<Response<Void>>()
    val deleteDonation: LiveData<Response<Void>> = _deleteDonation


    fun goToCreateDonation() {
        navigateFragment(navAction = R.id.nav_action_createUpdateDonationFragment_global)
    }

    fun goToDetailDonation(donation: Donations) {
        Bundle().apply {
            putSerializable("donation", donation as Serializable)
            navigateFragment(
                navAction = R.id.nav_action_detailDonationFragment_global,
                bundle = this
            )
        }
    }


    fun getDonation(uuid: String?) {
        viewModelScope.launch {
            donationRepository.getDonationsFromFirestore(uuid).collect { response ->
                _donations.postValue(response)
            }
        }
    }

    fun deleteDonation(donation: Donations) {
        donation.apply {
            this.enable = false
        }
        viewModelScope.launch {
            donationRepository.updateDonationInFirestore(donation).collect {
                _deleteDonation.postValue(it)
            }
        }
    }
}