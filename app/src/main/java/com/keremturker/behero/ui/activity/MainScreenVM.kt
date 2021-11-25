package com.keremturker.behero.ui.activity

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(private val auth: FirebaseAuth, myApp: Application) :
    BaseViewModel(app = myApp) {

    fun isUserLogin() = auth.currentUser != null
}