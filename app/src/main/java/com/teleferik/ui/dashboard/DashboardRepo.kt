package com.teleferik.ui.dashboard


import com.teleferik.base.BaseRepo
import com.teleferik.data.network.apisInterfaces.ApisService


class DashboardRepo(private val api: ApisService) : BaseRepo() {

    suspend fun ticketReservationsList(status:String?) = safeApiCalls {
        api.ticketReservationsList(status)
    }
}