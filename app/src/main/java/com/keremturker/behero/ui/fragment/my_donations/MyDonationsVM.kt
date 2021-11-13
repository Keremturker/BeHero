package com.keremturker.behero.ui.fragment.my_donations

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDonationsVM @Inject constructor(app: Application) : BaseViewModel(app)