package com.keremturker.behero.ui.fragment.register

import android.app.Application
import com.keremturker.behero.R
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterVM @Inject constructor(app: Application) : BaseViewModel(app){


    fun goToLogin(){
        navigateFragment(R.id.nav_action_loginFragment_global)
    }

    fun  goToMaps(){
        navigateFragment(R.id.nav_action_mapsFragment_global)
    }
}