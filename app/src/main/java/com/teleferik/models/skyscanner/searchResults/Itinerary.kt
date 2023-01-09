package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Itinerary(
    @SerializedName("BookingDetailsLink")
    @Expose
    val bookingDetailsLink: BookingDetailsLink?,
    @SerializedName("InboundLegId")
    @Expose
    val inboundLegId: String?,
    @SerializedName("OutboundLegId")
    @Expose
    val outboundLegId: String?,
    @SerializedName("PricingOptions")
    @Expose
    val pricingOptions: List<PricingOption>?
):Parcelable