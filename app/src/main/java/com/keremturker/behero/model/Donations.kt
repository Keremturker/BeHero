package com.keremturker.behero.model

import java.io.Serializable

data class Donations(
    val uuid: String = "",
    var patientName: String = "",
    var hospitalName: String = "",
    var description: String = "",
    var phone: String = "",
    var bloodGroup: String = "",
    var address: Address = Address(),
    val createTime: Any? = null,
    var updateTime: Any? = null,
) : Serializable