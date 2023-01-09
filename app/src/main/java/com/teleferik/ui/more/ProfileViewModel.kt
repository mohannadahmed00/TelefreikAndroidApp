package com.teleferik.ui.more


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleferik.data.network.Resource
import com.teleferik.models.BaseResponse
import com.teleferik.models.EditPhoneRequest
import com.teleferik.models.EditProfileRequest
import com.teleferik.models.editPhoneResponse.EditPhoneResponse
import com.teleferik.models.login.LoginResponse
import com.teleferik.models.notificationList.NotificationListResponse
import com.teleferik.models.showProfile.ShowProfileResponse
import com.teleferik.models.tickets.CreateTicketBodyRequest
import com.teleferik.models.tickets.SingleTicketResponse
import com.teleferik.models.tickets.Ticket
import com.teleferik.models.tickets.TicketsForCustomerResponse
import kotlinx.coroutines.launch
import java.io.File


class ProfileViewModel(private val profileRepo: ProfileRepo) : ViewModel() {

    val _showProfileResponse = MutableLiveData<Resource<BaseResponse<ShowProfileResponse>>>()
    val showProfileResponse: LiveData<Resource<BaseResponse<ShowProfileResponse>>> get() = _showProfileResponse

    val _editProfileResponse = MutableLiveData<Resource<BaseResponse<LoginResponse>>>()
    val editProfileResponse: LiveData<Resource<BaseResponse<LoginResponse>>> get() = _editProfileResponse

    val _editPhoneResponse = MutableLiveData<Resource<BaseResponse<EditPhoneResponse>>>()
    val editPhoneResponse: LiveData<Resource<BaseResponse<EditPhoneResponse>>> get() = _editPhoneResponse

    val _createTicketResponse = MutableLiveData<Resource<BaseResponse<Ticket>>>()
    val createTicketResponse: LiveData<Resource<BaseResponse<Ticket>>> get() = _createTicketResponse

    val _ticketsForCustomerResponse = MutableLiveData<Resource<BaseResponse<TicketsForCustomerResponse>>>()
    val ticketsForCustomerResponse: LiveData<Resource<BaseResponse<TicketsForCustomerResponse>>> get() = _ticketsForCustomerResponse

    val _singleTicketResponse = MutableLiveData<Resource<BaseResponse<SingleTicketResponse>>>()
    val singleTicketResponse: LiveData<Resource<BaseResponse<SingleTicketResponse>>> get() = _singleTicketResponse

    val _notificationListResponse = MutableLiveData<Resource<BaseResponse<NotificationListResponse>>>()
    val notificationListResponse: LiveData<Resource<BaseResponse<NotificationListResponse>>> get() = _notificationListResponse

    val _addReplyResponse = MutableLiveData<Resource<BaseResponse<Ticket>>>()
    val addReplyResponse: LiveData<Resource<BaseResponse<Ticket>>> get() = _addReplyResponse

    val _updateNotificationResponse = MutableLiveData<Resource<BaseResponse<Any>>>()
    val updateNotificationResponse: LiveData<Resource<BaseResponse<Any>>> get() = _updateNotificationResponse

    fun verifyOTP(OTP:String) = viewModelScope.launch {
        _editPhoneResponse.value = Resource.Loading
        _editPhoneResponse.value = profileRepo.verifyOTP(OTP)
    }

    fun resendOTP() = viewModelScope.launch {
        _editPhoneResponse.value = Resource.Loading
        _editPhoneResponse.value = profileRepo.resendOTP()
    }

    fun showProfile() = viewModelScope.launch {
        _showProfileResponse.value = Resource.Loading
        _showProfileResponse.value = profileRepo.showProfile()
    }

    fun editUserProfile(name:String ,email:String, file:File?) = viewModelScope.launch {
        _editProfileResponse.value = Resource.Loading
        _editProfileResponse.value = profileRepo.editUserProfile(name,email,file)
    }

    fun editUserMobile(editPhoneRequest: EditPhoneRequest) = viewModelScope.launch {
        _editPhoneResponse.value = Resource.Loading
        _editPhoneResponse.value = profileRepo.editUserMobile(editPhoneRequest)
    }

    fun createTicket(createTicketBodyRequest: CreateTicketBodyRequest) = viewModelScope.launch {
        _createTicketResponse.value = Resource.Loading
        _createTicketResponse.value = profileRepo.createTicket(createTicketBodyRequest)
    }
    fun getTicketsForCustomer() = viewModelScope.launch {
        _ticketsForCustomerResponse.value = Resource.Loading
        _ticketsForCustomerResponse.value = profileRepo.getTicketsForCustomer()
    }
    fun getSingleTicket(ticketId: String) = viewModelScope.launch {
        _singleTicketResponse.value = Resource.Loading
        _singleTicketResponse.value = profileRepo.getSingleTicket(ticketId)
    }
    fun addReply(ticket_id: String?, user_id: String?, file: File?, message: String?) = viewModelScope.launch {
        _addReplyResponse.value = Resource.Loading
        _addReplyResponse.value = profileRepo.addReply(ticket_id, user_id, file, message)
    }

    fun notificationsList() = viewModelScope.launch {
        _notificationListResponse.value = Resource.Loading
        _notificationListResponse.value = profileRepo.notificationsList()
    }

    fun updateNotification(notificationId:String) = viewModelScope.launch {
        _updateNotificationResponse.value = Resource.Loading
        _updateNotificationResponse.value = profileRepo.updateNotification(notificationId)
    }
}