package com.teleferik.models

import android.os.Build
import com.teleferik.utils.Constants.DEVICE_TYPE
import com.teleferik.utils.Constants.EGYPT_PHONE_CODE


data class EditPhoneRequest(
    val mobile:String,
    val new_firebase_token:String,
    val phonecode : String = EGYPT_PHONE_CODE
)