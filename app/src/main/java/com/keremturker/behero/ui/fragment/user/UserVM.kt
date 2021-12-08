package com.keremturker.behero.ui.fragment.user

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Users
import com.keremturker.behero.repository.AuthRepository
import com.keremturker.behero.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    app: Application
) : BaseViewModel(app) {

    fun navToProfileEdit() {
        navigateFragment(R.id.nav_action_profileEditFragment_global)
    }

    fun signOut() {
        authRepository.firebaseSignOut()
    }

    fun setAvailableDonation(user: Users) {
        viewModelScope.launch {
            profileRepository.createUserInFirestore(user).collect {}
        }
    }
}