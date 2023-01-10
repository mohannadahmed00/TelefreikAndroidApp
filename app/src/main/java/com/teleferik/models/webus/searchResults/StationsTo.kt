package com.teleferik.models.webus.searchResults

data class StationsTo(
    val city_id: String,
    val created_at: String,
    val deleted_at: Any,
    val description: Any,
    val id: Int,
    val `internal`: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val pivot: PivotX,
    val translations: List<TranslationXXXXXX>,
    val updated_at: String
)