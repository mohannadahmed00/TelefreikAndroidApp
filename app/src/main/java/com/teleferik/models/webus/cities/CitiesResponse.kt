package com.teleferik.models.webus.cities


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
@Parcelize
data class CitiesResponse(
    @SerializedName("data")
    @Expose
    val cities: MutableList<City>?
):Parcelable