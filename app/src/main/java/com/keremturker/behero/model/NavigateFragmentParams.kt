package com.keremturker.behero.model

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator

data class NavigateFragmentParams(val navAction: Int,
                                  val bundle: Bundle? = null,
                                  val navOptions: NavOptions? = null,
                                  val extras: FragmentNavigator.Extras? = null)