package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightNumber(
    @SerializedName("CarrierId")
    @Expose
    val carrierId: Int?,
    @SerializedName("FlightNumber")
    @Expose
    val flightNumber: String?
):Parcelable