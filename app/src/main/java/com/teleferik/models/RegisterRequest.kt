package com.teleferik.models

import android.os.Build
import com.teleferik.utils.Constants.DEVICE_TYPE
import com.teleferik.utils.Constants.EGYPT_PHONE_CODE

data class RegisterRequest(
    val mobile:String,
    val email:String,
    val name:String,
    val firebase_token:String,
    val loggedBy:String = "default",
    val SUUID: String? = null,
    val os_version:String = "V${Build.VERSION.SDK_INT}",
    val os_system:String = DEVICE_TYPE,
    val phonecode: String = EGYPT_PHONE_CODE,

)