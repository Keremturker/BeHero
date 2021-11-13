package com.keremturker.behero.ui.fragment.main

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainMV @Inject constructor(app: Application) : BaseViewModel(app)