package com.teleferik.models.bus.locations

data class LocationsResponseItem(
    val id: Int,
    val name: String,
    val name_ar: String,
    val name_en: String,
    val stations: List<Station>
)