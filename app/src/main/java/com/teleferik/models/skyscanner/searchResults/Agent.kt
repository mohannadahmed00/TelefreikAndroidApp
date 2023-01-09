package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Agent(
    @SerializedName("Id")
    @Expose
    val id: Int?,
    @SerializedName("ImageUrl")
    @Expose
    val imageUrl: String?,
    @SerializedName("Name")
    @Expose
    val name: String?,
    @SerializedName("OptimisedForMobile")
    @Expose
    val optimisedForMobile: Boolean?,
    @SerializedName("Status")
    @Expose
    val status: String?,
    @SerializedName("Type")
    @Expose
    val type: String?
):Parcelable