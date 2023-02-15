package com.teleferik.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T: Any?> (

        /*@SerializedName("status")
        @Expose*/
        val status: Int,
        /*@SerializedName("message")
        @Expose*/
        val message: String,
        /*@SerializedName("errors")
        @Expose*/
        //val errors: T?,
        /*@SerializedName("data")
        @Expose*/
        val data: T?,
        val need_verfication:Boolean?
)

class Errors(
        @SerializedName("mobile")
        @Expose
        val mobile:String?
)

