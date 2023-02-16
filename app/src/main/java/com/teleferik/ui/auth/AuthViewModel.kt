package com.teleferik.ui.auth


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleferik.data.network.Resource
import com.teleferik.models.BaseResponse
import com.teleferik.models.RegisterRequest
import com.teleferik.models.login.LoginResponse
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepo: AuthRepo) : ViewModel() {

    val _loginResponse = MutableLiveData<Resource<BaseResponse<LoginResponse>>>()
    val loginResponse: LiveData<Resource<BaseResponse<LoginResponse>>> get() = _loginResponse

    val _registerResponse = MutableLiveData<Resource<BaseResponse<LoginResponse>>>()
    val registerResponse: LiveData<Resource<BaseResponse<LoginResponse>>> get() = _registerResponse

    val _otpResponse = MutableLiveData<Resource<BaseResponse<LoginResponse>>>()
    val otpResponse: LiveData<Resource<BaseResponse<LoginResponse>>> get() = _otpResponse

    val _loginResponseArr = MutableLiveData<Resource<BaseResponse<List<Any?>>>>()
    val loginResponseArr: LiveData<Resource<BaseResponse<List<Any?>>>> get() = _loginResponseArr

    val _otpResponseArr = MutableLiveData<Resource<BaseResponse<List<Any?>>>>()
    val otpResponseArr: LiveData<Resource<BaseResponse<List<Any?>>>> get() = _otpResponseArr



    fun login(phone:String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        Log.e("ResponseLoginNumber",phone)
        _loginResponse.value = authRepo.login(phone)
    }

    fun sendOTP(phone:String) = viewModelScope.launch {
        _otpResponse.value = Resource.Loading
        _otpResponse.value = authRepo.sendOTP(phone)
    }

    fun verifyOTP(phone: String,phoneCode:String,code:String) = viewModelScope.launch {
        _otpResponse.value = Resource.Loading
        _otpResponse.value = authRepo.verifyOTP(phone,phoneCode,code)
    }

    fun register(registerRequest: RegisterRequest) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        Log.e("ResponseRegisterNumber",registerRequest.toString())
        _registerResponse.value = authRepo.register(registerRequest)
    }

    /*fun oldVerifyOTP(phone: String,phoneCode:String,code:String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.oldVerifyOTP(phone,phoneCode,code)
    }*/



    fun socialRegister(registerRequest: RegisterRequest) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.socialRegister(registerRequest)
    }



    fun resendOTP() = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.resendOTP()
    }
}