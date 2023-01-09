package com.teleferik.models

data class BaseResponse<T: Any> (
        val status: Boolean,
        val message: String,
        val data: T?
)

