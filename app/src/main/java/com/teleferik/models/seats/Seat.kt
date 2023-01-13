package com.teleferik.models.seats

import com.teleferik.ui.seatSelection.compose.Status

enum class Status{
    Reserved,
    Available,
    Selected
}

data class Seat(
    var num : Int,
    var status: Status = Status.Available
)
