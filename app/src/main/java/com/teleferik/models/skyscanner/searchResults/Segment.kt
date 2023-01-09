package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Segment(
    @SerializedName("ArrivalDateTime")
    @Expose
    val arrivalDateTime: String?,
    @SerializedName("Carrier")
    @Expose
    val carrier: Int?,
    @SerializedName("DepartureDateTime")
    @Expose
    val departureDateTime: String?,
    @SerializedName("DestinationStation")
    @Expose
    val destinationStation: Int?,
    @SerializedName("Directionality")
    @Expose
    val directionality: String?,
    @SerializedName("Duration")
    @Expose
    val duration: Int?,
    @SerializedName("FlightNumber")
    @Expose
    val flightNumber: String?,
    @SerializedName("Id")
    @Expose
    val id: Int?,
    @SerializedName("JourneyMode")
    @Expose
    val journeyMode: String?,
    @SerializedName("OperatingCarrier")
    @Expose
    val operatingCarrier: Int?,
    @SerializedName("OriginStation")
    @Expose
    val originStation: Int?
):Parcelable