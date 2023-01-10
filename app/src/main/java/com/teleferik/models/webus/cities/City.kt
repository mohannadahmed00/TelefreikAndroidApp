package com.teleferik.models.webus.cities

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    @SerializedName("c2_order")
    @Expose
    val c2_order: String,
    @SerializedName("c_order")
    @Expose
    val c_order: String,
    @SerializedName("created_at")
    @Expose
    val created_at: String,
    @SerializedName("deleted_at")
    @Expose
    val deleted_at: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("internal")
    @Expose
    val internal: String?,
    @SerializedName("latitude")
    @Expose
    val latitude: String,
    @SerializedName("longitude")
    @Expose
    val longitude: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("translations")
    @Expose
    val translations: List<Translation>,
    @SerializedName("travel")
    @Expose
    val travel: String,
    @SerializedName("updated_at")
    @Expose
    val updated_at: String
):Parcelable