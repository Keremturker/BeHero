package com.keremturker.behero.model

import java.io.Serializable

data class Users(
    val uuid: String = "",
    val name: String = "",
    val mail: String = "",
    val phone: String = "",
    val birthDay: String = "",
    val gender: String = "",
    val bloodGroup: String = "",
    val address: String = "",
    val shortAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val availableDonate: Boolean = false,
    val timestamp: Any? = null
) : Serializable