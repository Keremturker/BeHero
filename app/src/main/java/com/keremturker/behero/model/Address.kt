package com.keremturker.behero.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Address(
    val description: String? = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val cityName: String? = "",
    val subCityName: String? = "",
    val countryName: String? = "",
    val countryCode: String? = ""
) : Serializable,Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(cityName)
        parcel.writeString(subCityName)
        parcel.writeString(countryName)
        parcel.writeString(countryCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}