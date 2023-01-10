package com.teleferik.models.webus.cities

data class Translation(
    val city_id: String,
    val created_at: String,
    val description: Any,
    val id: Int,
    val locale: String,
    val name: String,
    val updated_at: String
)