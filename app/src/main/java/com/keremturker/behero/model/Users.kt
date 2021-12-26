package com.keremturker.behero.model

import android.os.Parcel
import android.os.Parcelable
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
    var mailVerified: Boolean = false,
    val createTime: Any? = null,
    var updateTime: Any? = null
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(Address::class.java.classLoader),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeString(name)
        parcel.writeString(mail)
        parcel.writeString(phone)
        parcel.writeString(birthDay)
        parcel.writeString(gender)
        parcel.writeString(bloodGroup)
        parcel.writeParcelable(address, flags)
        parcel.writeByte(if (availableDonate) 1 else 0)
        parcel.writeByte(if (mailVerified) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }

}