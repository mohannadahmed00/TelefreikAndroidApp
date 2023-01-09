package com.teleferik.models.tickets


import com.google.gson.annotations.SerializedName

data class SingleTicketResponse(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("customer")
    val customer: Customer?,
    @SerializedName("customer_id")
    val customerId: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("replies")
    val replies: List<Reply>?,
    @SerializedName("section")
    val section: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)