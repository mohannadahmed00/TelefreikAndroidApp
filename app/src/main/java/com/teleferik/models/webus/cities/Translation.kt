package com.teleferik.models.webus.cities

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Translation(
    @SerializedName("city_id")
    @Expose
    val city_id: String,
    @SerializedName("created_at")
    @Expose
    val created_at: String,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("locale")
    @Expose
    val locale: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("updated_at")
    @Expose
    val updated_at: String
) : Parcelable