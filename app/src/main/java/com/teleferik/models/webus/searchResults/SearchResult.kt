package com.teleferik.models.webus.searchResults

data class SearchResult(
    val bus: Bus,
    val category: String,
    val cities_from: CitiesFrom,
    val cities_to: CitiesTo,
    val code: String,
    val date: String,
    val driver: Any,
    val id: Int,
    val persons: String,
    val price: String,
    val search_from: SearchFrom,
    val search_to: SearchTo,
    val seats: List<Seat>,
    val stations_from: List<StationsFrom>,
    val stations_to: List<StationsTo>,
    val status: String,
    val time: String
)