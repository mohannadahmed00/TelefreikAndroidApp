package com.teleferik.models.showProfile


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Country(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("iso")
    @Expose
    val iso: String?,
    @SerializedName("iso3")
    @Expose
    val iso3: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("nicename")
    @Expose
    val nicename: String?,
    @SerializedName("numcode")
    @Expose
    val numcode: String?,
    @SerializedName("phonecode")
    @Expose
    val phonecode: String?
)