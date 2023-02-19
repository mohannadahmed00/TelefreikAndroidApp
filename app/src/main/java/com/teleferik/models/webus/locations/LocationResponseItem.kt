package com.teleferik.models.webus.locations

data class LocationResponseItem(
    val id: Int,
    val name: String,
    val stations: List<Any>
)