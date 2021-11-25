package com.keremturker.behero.model

import java.io.Serializable

data class Address(
    val description: String="",
    val latitude: Double=0.0,
    val longitude: Double=0.0
):Serializable