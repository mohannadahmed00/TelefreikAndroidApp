package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Query(
    @SerializedName("Adults")
    @Expose
    val adults: Int?,
    @SerializedName("CabinClass")
    @Expose
    val cabinClass: String?,
    @SerializedName("Children")
    @Expose
    val children: Int?,
    @SerializedName("Country")
    @Expose
    val country: String?,
    @SerializedName("Currency")
    @Expose
    val currency: String?,
    @SerializedName("DestinationPlace")
    @Expose
    val destinationPlace: String?,
    @SerializedName("GroupPricing")
    @Expose
    val groupPricing: Boolean?,
    @SerializedName("InboundDate")
    @Expose
    val inboundDate: String?,
    @SerializedName("Infants")
    @Expose
    val infants: Int?,
    @SerializedName("Locale")
    @Expose
    val locale: String?,
    @SerializedName("LocationSchema")
    @Expose
    val locationSchema: String?,
    @SerializedName("OriginPlace")
    @Expose
    val originPlace: String?,
    @SerializedName("OutboundDate")
    @Expose
    val outboundDate: String?
):Parcelable