package com.teleferik.models.ticketReservation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.teleferik.models.promotionalOffer.Offer

data class TicketReservation(
    @SerializedName("reservations")
    @Expose
    val reservations: List<SingleTicketReservation>,

    @SerializedName("reservation")
    @Expose
    val reservation: SingleTicketReservation
)
