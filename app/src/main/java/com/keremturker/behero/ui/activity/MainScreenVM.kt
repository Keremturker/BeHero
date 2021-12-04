package com.keremturker.behero.ui.activity

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.keremturker.behero.base.BaseViewModel
import com.keremturker.behero.repository.AuthRepository
import com.keremturker.behero.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val authRepository: AuthRepository,
    myApp: Application
) :
    BaseViewModel(app = myApp) {

    private val _isLoginUser = SingleLiveEvent<Boolean>()
    val isLoginUser: LiveData<Boolean> = _isLoginUser


    @ExperimentalCoroutinesApi
    fun getAuthState() {
        viewModelScope.launch {
            authRepository.getFirebaseAuthState().collect {
                _isLoginUser.postValue(it)
            }
        }
    }
}