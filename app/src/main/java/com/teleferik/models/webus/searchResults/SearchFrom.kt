package com.teleferik.models.webus.searchResults

data class SearchFrom(
    val c2_order: String,
    val c_order: String,
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val image: String,
    val `internal`: Any,
    val latitude: String,
    val longitude: String,
    val name: String,
    val status: String,
    val translations: List<Translation>,
    val travel: String,
    val updated_at: String
)