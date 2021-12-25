package com.keremturker.behero.ui.fragment.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Donations
import com.keremturker.behero.model.Response
import com.keremturker.behero.repository.DonationRepository
import com.keremturker.behero.repository.UsersRepository
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMV
@Inject constructor(
    app: Application,
    private val donationRepository: DonationRepository,
    private val usersRepository: UsersRepository
) : BaseViewModel(app) {


    private val _countDonation = SingleLiveEvent<Response<Int>>()
    val countDonation: LiveData<Response<Int>> = _countDonation

    private val _countDonor = SingleLiveEvent<Response<Int>>()
    val countDonor: LiveData<Response<Int>> = _countDonor

    private val _lastDonation = SingleLiveEvent<Response<List<Donations>>>()
    val lastDonation: LiveData<Response<List<Donations>>> = _lastDonation


    init {
        getAllDonationCount()
        getAllDonorCount()
        getLastDonation()
    }

    private fun getAllDonationCount() {
        viewModelScope.launch {
            donationRepository.getAllDonationCount().collect {
                _countDonation.postValue(it)
            }
        }
    }

    private fun getAllDonorCount() {
        viewModelScope.launch {
            usersRepository.getAllDonorCount().collect {
                _countDonor.postValue(it)
            }
        }
    }

    private fun getLastDonation() {
        viewModelScope.launch {
            donationRepository.getLastDonation().collect {
                _lastDonation.postValue(it)
            }
        }
    }
}