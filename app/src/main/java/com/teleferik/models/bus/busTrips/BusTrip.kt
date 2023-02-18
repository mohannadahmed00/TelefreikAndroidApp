package com.teleferik.models.bus.busTrips

data class BusTrip(
    val bus: Bus,
    val category: String,
    val cities_from: List<CitiesFrom>,
    val cities_to: List<CitiesTo>,
    val date: String,
    val gateway_id: String,
    val id: Int,
    val stations_from: List<StationsFrom>,
    val stations_to: List<StationsTo>,
    val time: String
)