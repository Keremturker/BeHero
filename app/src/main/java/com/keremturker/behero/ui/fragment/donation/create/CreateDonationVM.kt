package com.keremturker.behero.ui.fragment.donation.create

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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
    app: Application,
    private val donationRepository: DonationRepository
) : BaseViewModel(app) {

    private val _createDonation = SingleLiveEvent<Response<Void>>()
    val createDonation: LiveData<Response<Void>> = _createDonation


    fun createDonation(donation: Donations) {
        viewModelScope.launch {
            donationRepository.createDonationInFirestore(donation).collect {
                _createDonation.postValue(it)
            }
        }
    }
}