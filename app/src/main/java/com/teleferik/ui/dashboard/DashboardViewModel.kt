package com.teleferik.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleferik.data.network.Resource
import com.teleferik.models.BaseResponse
import com.teleferik.models.ticketReservation.TicketReservation
import kotlinx.coroutines.launch

class DashboardViewModel(private val dashboardRepo: DashboardRepo) : ViewModel() {
    val _ticketReservationList = MutableLiveData<Resource<BaseResponse<TicketReservation>>>()
    val ticketReservationList: LiveData<Resource<BaseResponse<TicketReservation>>> get() = _ticketReservationList

    fun ticketReservationsList(status:String?) = viewModelScope.launch {
        _ticketReservationList.value = Resource.Loading
        _ticketReservationList.value = dashboardRepo.ticketReservationsList(status)
    }
}