package com.keremturker.behero.model

import java.io.Serializable

data class Users(
    val uuid: String = "",
    var name: String = "",
    var mail: String = "",
    var phone: String = "",
    var birthDay: String = "",
    var gender: String = "",
    var bloodGroup: String = "",
    var address: String = "",
    var shortAddress: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var availableDonate: Boolean = false,
    val timestamp: Any? = null
) : Serializable