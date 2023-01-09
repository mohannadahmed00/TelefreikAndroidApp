package com.teleferik.models.skyscanner.airPorts


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    @SerializedName("CityId")
    @Expose
    val cityId: String?,
    @SerializedName("CountryId")
    @Expose
    val countryId: String?,
    @SerializedName("CountryName")
    @Expose
    val countryName: String?,
    @SerializedName("PlaceId")
    @Expose
    val placeId: String?,
    @SerializedName("PlaceName")
    @Expose
    val placeName: String?,
    @SerializedName("RegionId")
    @Expose
    val regionId: String?,
    var isSelected: Boolean = false
):Parcelable