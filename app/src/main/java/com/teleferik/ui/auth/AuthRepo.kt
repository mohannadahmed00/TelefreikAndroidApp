package com.teleferik.ui.auth

import com.teleferik.base.BaseRepo
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.models.RegisterRequest
import com.teleferik.utils.Constants

class AuthRepo(private val api: ApisService) : BaseRepo() {

    suspend fun login(phone:String) = safeApiCalls {
        api.login(phone,Constants.EGYPT_PHONE_CODE)
    }

    suspend fun register(registerRequest: RegisterRequest) = safeApiCalls {
        api.register(registerRequest)
    }

    suspend fun socialRegister(registerRequest: RegisterRequest) = safeApiCalls {
        api.socialRegister(registerRequest)
    }

    suspend fun verifyOTP(OTP:String) = safeApiCalls {
        api.verifyOTP(OTP)
    }

    suspend fun resendOTP() = safeApiCalls {
        api.resendOTP()
    }
}