package com.teleferik.models.webus.cities


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize


data class CitiesResponse(
    val cities: MutableList<City>?
)