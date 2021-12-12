package com.keremturker.behero.ui.fragment.donor

import android.app.Application
import com.keremturker.behero.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchDonorVM @Inject constructor(
    app: Application
) : BaseViewModel(app) {
}