package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Leg(
    @SerializedName("Arrival")
    @Expose
    val arrival: String?,
    @SerializedName("Carriers")
    @Expose
    val carriers: List<Int>?,
    @SerializedName("Departure")
    @Expose
    val departure: String?,
    @SerializedName("DestinationStation")
    @Expose
    val destinationStation: Int?,
    @SerializedName("Directionality")
    @Expose
    val directionality: String?,
    @SerializedName("Duration")
    @Expose
    val duration: Int?,
    @SerializedName("FlightNumbers")
    @Expose
    val flightNumbers: List<FlightNumber>?,
    @SerializedName("Id")
    @Expose
    val id: String?,
    @SerializedName("JourneyMode")
    @Expose
    val journeyMode: String?,
    @SerializedName("OperatingCarriers")
    @Expose
    val operatingCarriers: List<Int>?,
    @SerializedName("OriginStation")
    @Expose
    val originStation: Int?,
    @SerializedName("SegmentIds")
    @Expose
    val segmentIds: List<Int>?,
    @SerializedName("Stops")
    @Expose
    val stops: List<Int>?
):Parcelable