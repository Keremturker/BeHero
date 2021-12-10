package com.keremturker.behero.model

import java.io.Serializable

data class Donations(
    val uuid: String = "",
    var phone: String = "",
    var bloodGroup: String = "",
    var address: String = "",
    var shortAddress: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var description: String = "",
    val createTime: Any? = null,
    var updateTime: Any? = null
) : Serializable