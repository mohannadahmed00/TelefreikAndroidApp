package com.teleferik.models.signupError


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SignupErrorResponse(
    @SerializedName("errors")
    @Expose
    val errors: Errors?,
    @SerializedName("message")
    @Expose
    val message: String?
)