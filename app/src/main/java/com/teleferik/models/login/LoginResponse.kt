package com.teleferik.models.login


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResponse(
    @SerializedName("OTP")
    @Expose
    val OTP: String?,
    @SerializedName("SUUID")
    @Expose
    val SUUID: String?,
    @SerializedName("loggedBy")
    @Expose
    val loggedBy: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("mobile")
    @Expose
    val mobile: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("status")
    @Expose
    val status: Any?,
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?,
    @SerializedName("user_image")
    @Expose
    val userImage: Any?
)