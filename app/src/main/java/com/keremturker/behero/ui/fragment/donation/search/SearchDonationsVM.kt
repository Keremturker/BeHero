package com.keremturker.behero.ui.fragment.donation.search

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
class SearchDonationsVM
@Inject constructor(
    app: Application,
    private val donationRepository: DonationRepository
) : BaseViewModel(app) {

    private val _donations = SingleLiveEvent<Response<List<Donations>>>()
    val donations: LiveData<Response<List<Donations>>> = _donations

    fun goToDetailDonation(donation: Donations) {
        Bundle().apply {
            putSerializable("donation", donation as Serializable)
            navigateFragment(
                navAction = R.id.nav_action_detailDonationFragment_global,
                bundle = this
            )
        }
    }

    fun getDonations(bloodGroup: String, address: String, limit: Long? = null) {
        viewModelScope.launch {
            donationRepository.getDonationsFromFirestore(
                bloodGroup = bloodGroup,
                address = address,
                limit = limit
            ).collect {
                _donations.postValue(it)
            }
        }
    }
}