package com.teleferik.models.editPhoneResponse


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class EditPhoneResponse(
    @SerializedName("country_id")
    @Expose
    val countryId: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("customer_details_id")
    @Expose
    val customerDetailsId: Int?,
    @SerializedName("email")
    @Expose
    val email: String?,
    @SerializedName("OTP")
    @Expose
    val OTP: String?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("image")
    @Expose
    val image: Any?,
    @SerializedName("mobile")
    @Expose
    val mobile: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("password")
    @Expose
    val password: Any?,
    @SerializedName("phone")
    @Expose
    val phone: Any?,
    @SerializedName("status")
    @Expose
    val status: Any?,
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?
)