package com.keremturker.behero.utils

import android.Manifest

object Constants {
    const val PERMISSION_LOCATION = 0
    const val ADDRESS="ADDRESS"

    //Messages
    const val ERROR_MESSAGE = "Unexpected error!"

    //References
    const val USERS_REF = "users"

    val permissionLocation = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

}