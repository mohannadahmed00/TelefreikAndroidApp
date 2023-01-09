package com.teleferik.models.tickets


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("role_id")
    val roleId: Int?,
    @SerializedName("state_id")
    val stateId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_image")
    val userImage: String?
)