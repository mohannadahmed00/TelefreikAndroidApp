package com.teleferik.models.tickets


import com.google.gson.annotations.SerializedName

data class Reply(
    @SerializedName("attachment")
    val attachment: String? = null ,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: String?= null ,
    @SerializedName("message")
    val message: String?,
    @SerializedName("ticket_id")
    val ticketId: String? =null ,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("user")
    val user: User? = null ,
    @SerializedName("user_id")
    val userId: String? = null
)