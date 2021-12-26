package com.keremturker.behero.ui.fragment.user

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.AuthRepository
import com.keremturker.behero.repository.DonationRepository
import com.keremturker.behero.repository.ProfileRepository
import com.keremturker.behero.utils.SharedHelper
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val donationRepository: DonationRepository,
    private val sharedHelper: SharedHelper,
    app: Application
) : BaseViewModel(app) {

    private val _countDonation = SingleLiveEvent<Response<Int>>()
    val countDonation: LiveData<Response<Int>> = _countDonation

    fun goToProfileEdit() {
        navigateFragment(R.id.nav_action_profileEditFragment_global)
    }

    fun goToDonation() {
        val user = sharedHelper.syncUsers
        Bundle().apply {
            putSerializable("users", user as Serializable)
            navigateFragment(
                navAction = R.id.nav_action_donationsFragment_global,
                bundle = this
            )
        }
    }

    fun signOut() {
        authRepository.firebaseSignOut()
    }

    fun setAvailableDonation(user: Users) {
        viewModelScope.launch {
            profileRepository.createOrUpdateUserInFirestore(user).collect {
                if (it is Response.Success) {
                    sharedHelper.syncUsers = user
                }
            }
        }
    }

    fun getDonationCount() {
        viewModelScope.launch {
            donationRepository.getDonationCount().collect {
                _countDonation.postValue(it)
            }
        }
    }
}