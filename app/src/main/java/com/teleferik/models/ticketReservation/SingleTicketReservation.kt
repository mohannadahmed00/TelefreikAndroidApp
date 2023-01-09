package com.teleferik.models.ticketReservation

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SingleTicketReservation(
    @SerializedName("id")
    @Expose
    val id: Long,
    @SerializedName("price")
    @Expose
    val price: Double,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("departure")
    @Expose
    val departure: String,
    @SerializedName("arrival")
    @Expose
    val arrival: String,
) : Parcelable