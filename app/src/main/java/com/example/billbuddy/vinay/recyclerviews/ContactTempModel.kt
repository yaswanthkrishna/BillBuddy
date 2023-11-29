package com.example.billbuddy.vinay.recyclerviews

import android.os.Parcel
import android.os.Parcelable

data class ContactTempModel(
    val number: String?,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(number)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactTempModel> {
        override fun createFromParcel(parcel: Parcel): ContactTempModel {
            return ContactTempModel(parcel)
        }

        override fun newArray(size: Int): Array<ContactTempModel?> {
            return arrayOfNulls(size)
        }
    }
}
