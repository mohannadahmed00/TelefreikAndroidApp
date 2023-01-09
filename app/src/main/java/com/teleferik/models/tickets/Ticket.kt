package com.teleferik.models.tickets


import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("customer_id")
    val customerId: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("section")
    val section: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)