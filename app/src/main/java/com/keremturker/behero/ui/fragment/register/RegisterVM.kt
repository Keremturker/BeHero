package com.keremturker.behero.ui.fragment.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.model.Response
import com.keremturker.behero.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(
    private val repository: AuthRepository,
    app: Application
) : BaseViewModel(app) {

    private val _signUpUser = MutableLiveData<Response<FirebaseUser>>()
    val signUpUser: LiveData<Response<FirebaseUser>> = _signUpUser

    fun goToLogin() {
        navigateFragment(R.id.nav_action_RemoveRegisterFragment_global)
    }

    fun goToMaps() {
        navigateFragment(R.id.nav_action_mapsFragment_global)
    }

    fun signUpWithMail() {
        viewModelScope.launch {
         repository.firebaseSignUpWithMail("keremturker@gmail.com", "123123123").collect {
                _signUpUser.postValue(it)
            }
        }
    }
}