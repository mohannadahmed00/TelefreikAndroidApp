package com.teleferik.models.webus.searchResults

data class StationsFrom(
    val city_id: String,
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val `internal`: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val pivot: Pivot,
    val translations: List<TranslationXXXXXX>,
    val updated_at: String
)