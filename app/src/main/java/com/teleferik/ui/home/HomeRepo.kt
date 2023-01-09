package com.teleferik.ui.home


import com.teleferik.base.BaseRepo
import com.teleferik.data.network.apisInterfaces.ApisService


class HomeRepo(private val api: ApisService) : BaseRepo() {

    suspend fun searchAirPorts(url: String) = safeApiCalls {
        api.searchAirPorts(url)
    }

    suspend fun getTripsSearchResults(url: String) = safeApiCalls {
        api.getTripsSearchResults(url)
    }

    suspend fun promotionalOffersList() = safeApiCalls {
        api.promotionalOffersList()
    }
}