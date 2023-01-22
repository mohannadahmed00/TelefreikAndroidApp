package com.teleferik.models.seats

data class Seat(
    var num : Int,
    var status: Status = Status.Available
)
