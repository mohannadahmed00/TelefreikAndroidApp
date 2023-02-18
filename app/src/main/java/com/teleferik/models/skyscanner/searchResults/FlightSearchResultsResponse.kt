package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightSearchResultsResponse(
    @SerializedName("Agents")
    @Expose
    val agents: List<Agent>?,
    @SerializedName("Carriers")
    @Expose
    val carriers: List<Carrier>?,
    @SerializedName("Currencies")
    @Expose
    val currencies: List<Currency>?,
    @SerializedName("Itineraries")
    @Expose
    val itineraries: List<Itinerary>?,
    @SerializedName("Legs")
    @Expose
    val legs: List<Leg>?,
    @SerializedName("Places")
    @Expose
    val places: List<Place>?,
    @SerializedName("Query")
    @Expose
    val query: Query?,
    @SerializedName("Segments")
    @Expose
    val segments: List<Segment>?,
    @SerializedName("SessionKey")
    @Expose
    val sessionKey: String?,
    @SerializedName("Status")
    @Expose
    val status: String?
):Parcelable