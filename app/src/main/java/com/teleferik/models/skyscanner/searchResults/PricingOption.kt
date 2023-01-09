package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PricingOption(
    @SerializedName("Agents")
    @Expose
    val agents: List<Int>?,
    @SerializedName("DeeplinkUrl")
    @Expose
    val deeplinkUrl: String?,
    @SerializedName("Price")
    @Expose
    val price: Double?,
    @SerializedName("QuoteAgeInMinutes")
    @Expose
    val quoteAgeInMinutes: Int?
):Parcelable