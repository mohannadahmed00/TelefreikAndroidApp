package com.teleferik.models.notificationList


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class NotificationListResponseItem(
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("customer_id")
    @Expose
    val customerId: Int?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("link")
    @Expose
    val link: String?,
    @SerializedName("pnotification_type_id")
    @Expose
    val pnotificationTypeId: Int?,
    @SerializedName("read")
    @Expose
    val read: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?
)