package com.keremturker.behero.ui.fragment.user

import android.app.Application
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val authRepository: AuthRepository,
    app: Application
) : BaseViewModel(app) {

    fun navToProfileEdit() {
        navigateFragment(R.id.nav_action_profileEditFragment_global)
    }

    fun signOut() {
        authRepository.firebaseSignOut()
    }
}