package com.teleferik.models.promotionalOffer


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Offer(
    @SerializedName("brief")
    @Expose
    val brief: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("title")
    @Expose
    val title: String
)