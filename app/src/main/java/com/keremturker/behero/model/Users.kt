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
    var address: Address? = null,
    var availableDonate: Boolean = false,
    val createTime: Any? = null,
    var updateTime: Any? = null
) : Serializable