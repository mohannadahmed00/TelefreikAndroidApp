package com.teleferik.ui.auth


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


    fun login(phone:String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.login(phone)
    }

    fun register(registerRequest: RegisterRequest) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.register(registerRequest)
    }

    fun socialRegister(registerRequest: RegisterRequest) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.socialRegister(registerRequest)
    }

    fun verifyOTP(OTP:String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.verifyOTP(OTP)
    }

    fun resendOTP() = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepo.resendOTP()
    }
}