package com.teleferik.models.showProfile


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ShowProfileResponse(
    @SerializedName("country")
    @Expose
    val country: Country?,
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
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("image")
    @Expose
    val image: String?,
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
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String?
)