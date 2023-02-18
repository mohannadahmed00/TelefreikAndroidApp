package com.teleferik.models.bus.locations

data class LocationsResponseItem(
    val id: Int,
    val name: String,
    val stations: List<Station>
)