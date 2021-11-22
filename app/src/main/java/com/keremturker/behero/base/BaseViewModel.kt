package com.keremturker.behero.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.keremturker.behero.model.NavigateFragmentParams
import com.keremturker.behero.utils.SingleLiveEvent

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    val navigateFragmentDetection by lazy { SingleLiveEvent<NavigateFragmentParams>() }
    val loadingDetection by lazy { SingleLiveEvent<Boolean>() }

    fun navigateFragment(
        navAction: Int,
        bundle: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: FragmentNavigator.Extras? = null
    ) {
        val params=NavigateFragmentParams(navAction, bundle, navOptions, extras)
        navigateFragmentDetection.postValue(params)
    }

}