package com.keremturker.behero.ui.activity

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(myApp: Application) :
    BaseViewModel(app = myApp)