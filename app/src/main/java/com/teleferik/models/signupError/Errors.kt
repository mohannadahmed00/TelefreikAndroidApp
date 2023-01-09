package com.teleferik.models.signupError


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Errors(
    @SerializedName("email")
    @Expose
    val email: List<String>?,
    @SerializedName("mobile")
    @Expose
    val mobile: List<String>?
)