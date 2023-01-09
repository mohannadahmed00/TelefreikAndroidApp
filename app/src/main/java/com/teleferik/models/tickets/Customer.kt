package com.teleferik.models.tickets


import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("country_id")
    val countryId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("customer_details_id")
    val customerDetailsId: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: Any?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("status")
    val status: Any?,
    @SerializedName("updated_at")
    val updatedAt: String?
)