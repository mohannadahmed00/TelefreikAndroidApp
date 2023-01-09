package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Carrier(
    @SerializedName("Code")
    @Expose
    val code: String?,
    @SerializedName("DisplayCode")
    @Expose
    val displayCode: String?,
    @SerializedName("Id")
    @Expose
    val id: Int?,
    @SerializedName("ImageUrl")
    @Expose
    val imageUrl: String?,
    @SerializedName("Name")
    @Expose
    val name: String?
):Parcelable