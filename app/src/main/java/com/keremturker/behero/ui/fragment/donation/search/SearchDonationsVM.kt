package com.keremturker.behero.ui.fragment.donation.search

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchDonationsVM @Inject constructor(app: Application) : BaseViewModel(app)