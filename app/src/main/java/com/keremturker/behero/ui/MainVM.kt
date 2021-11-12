package com.keremturker.behero.ui

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor( myApp: Application) :
    BaseViewModel(app = myApp)