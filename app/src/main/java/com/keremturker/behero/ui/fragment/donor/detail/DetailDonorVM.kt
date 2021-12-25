package com.keremturker.behero.ui.fragment.donor.detail

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.DonationRepository
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class DetailDonorVM @Inject constructor(
    app: Application,
    private val donationRepository: DonationRepository,
) : BaseViewModel(app) {


    private val _countDonation = SingleLiveEvent<Response<Int>>()
    val countDonation: LiveData<Response<Int>> = _countDonation


    fun getDonationCount(uuid: String) {
        viewModelScope.launch {
            donationRepository.getDonationCount(uuid = uuid).collect {
                _countDonation.postValue(it)
            }
        }
    }

    fun goToDonationList(user: Users) {
        Bundle().apply {
            putSerializable("users", user as Serializable)
            navigateFragment(
                navAction = R.id.nav_action_donationsFragment_global,
                bundle = this
            )
        }
    }
}