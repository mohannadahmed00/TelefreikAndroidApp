package com.teleferik.models.skyscanner.searchResults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    @SerializedName("Code")
    @Expose
    val code: String?,
    @SerializedName("Id")
    @Expose
    val id: Int?,
    @SerializedName("Name")
    @Expose
    val name: String?,
    @SerializedName("ParentId")
    @Expose
    val parentId: Int?,
    @SerializedName("Type")
    @Expose
    val type: String?
):Parcelable