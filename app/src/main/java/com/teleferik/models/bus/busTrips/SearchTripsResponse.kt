package com.teleferik.models.bus.busTrips

import com.teleferik.models.bus.busTrips.BusTrip
import com.teleferik.models.bus.busTrips.Errors

data class SearchTripsResponse(
    val `data`: List<BusTrip>,
    val errors: Errors,
    val message: String,
    val status: Int
)

data class BusTripDetailsResponse(
    val data: BusTrip,
    val errors: Errors,
    val message: String,
    val status: Int
)