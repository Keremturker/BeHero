package com.keremturker.behero.utils

import android.Manifest

object Constants {
    const val BASE_SHARED_PREF_KEY = "BASE_SHARED_PREF_KEY"
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm"

    const val PERMISSION_LOCATION = 0
    const val ADDRESS = "ADDRESS"
    const val DONATION = "DONATION"

    //Messages
    const val ERROR_MESSAGE = "Unexpected error!"

    //References
    const val USERS_REF = "users"
    const val DONATIONS_REF = "donations"

    val permissionLocation = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun emptyText() = ""
}