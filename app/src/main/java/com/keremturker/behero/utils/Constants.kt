package com.keremturker.behero.utils

import android.Manifest

object Constants {
    const val PERMISSION_LOCATION = 0

    val permissionLocation = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

}