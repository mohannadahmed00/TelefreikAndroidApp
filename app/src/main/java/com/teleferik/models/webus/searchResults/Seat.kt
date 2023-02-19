package com.teleferik.models.webus.searchResults

data class Seat(
    val bus_id: String,
    val created_at: String,
    val id: Int,
    val name: String,
    val slug: String,
    val status: String,
    val updated_at: String
)