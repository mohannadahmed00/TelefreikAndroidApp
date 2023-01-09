package com.teleferik.models.skyscanner.airPorts


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirPortsResponse(
    @SerializedName("Places")
    @Expose
    val places: MutableList<Place>?
):Parcelable