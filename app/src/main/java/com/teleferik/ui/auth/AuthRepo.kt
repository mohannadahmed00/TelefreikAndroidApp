package com.teleferik.ui.auth

import android.util.Log
import com.teleferik.base.BaseRepo
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.models.BaseResponse
import com.teleferik.models.RegisterRequest
import com.teleferik.models.login.LoginResponse
import com.teleferik.utils.Constants

class AuthRepo(private val api: ApisService) : BaseRepo() {

    suspend fun login(phone: String) :Resource<BaseResponse<LoginResponse>>{
        return safeApiCalls {
            Log.e("ResponseLoginNumber","${phone}..${Constants.EGYPT_PHONE_CODE}")
            api.login(phone, Constants.EGYPT_PHONE_CODE)
        }
    }


    suspend fun sendOTP(phone: String):Resource<BaseResponse<LoginResponse>>{
        return safeApiCalls {
            api.sendOTP(phone, Constants.EGYPT_PHONE_CODE)
        }
    }

    suspend fun verifyOTP(
        phone: String,
        phoneCode: String,
        code: String
    ): Resource<BaseResponse<LoginResponse>> {
        return safeApiCalls {
            api.verifyOTP(phone, phoneCode, code)
        }
    }

    suspend fun register(registerRequest: RegisterRequest):Resource<BaseResponse<LoginResponse>> {
        return safeApiCalls {
            api.register(registerRequest)
        }
    }

    /*suspend fun oldVerifyOTP(phone: String, phoneCode: String, code: String) = safeApiCalls {
        api.oldVerifyOTP(phone, phoneCode, code)
    }*/




    suspend fun socialRegister(registerRequest: RegisterRequest) = safeApiCalls {
        api.socialRegister(registerRequest)
    }


    suspend fun resendOTP() = safeApiCalls {
        api.resendOTP()
    }
}