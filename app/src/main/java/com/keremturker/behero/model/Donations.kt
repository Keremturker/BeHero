package com.keremturker.behero.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Donations(
    @DocumentId
    val documentId: String = "",
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