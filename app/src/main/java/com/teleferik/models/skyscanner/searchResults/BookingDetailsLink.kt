package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingDetailsLink(
    @SerializedName("Body")
    @Expose
    val body: String?,
    @SerializedName("Method")
    @Expose
    val method: String?,
    @SerializedName("Uri")
    @Expose
    val uri: String?
):Parcelable