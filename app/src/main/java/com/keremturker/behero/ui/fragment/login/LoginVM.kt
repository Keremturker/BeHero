package com.keremturker.behero.ui.fragment.login

import android.app.Application
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(app: Application) : BaseViewModel(app){


    fun goToRegister(){
        navigateFragment(R.id.nav_action_registerFragment_global)
    }
}