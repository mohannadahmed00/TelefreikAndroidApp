package com.teleferik.models.webus.searchResults

data class Pivot(
    val id: String,
    val key: String,
    val price: Any,
    val station_id: String,
    val time: String,
    val trip_id: String
)