package com.teleferik.ui.home


import com.teleferik.base.BaseRepo
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.models.BaseResponse
import com.teleferik.models.bus.locations.LocationsResponseItem


class HomeRepo(private val api: ApisService) : BaseRepo() {

    suspend fun searchAirPorts(url: String) = safeApiCalls {
        api.searchAirPorts(url)
    }

    suspend fun searchLocations():Resource<BaseResponse<List<LocationsResponseItem>>> {
        return safeApiCalls {
            api.searchLocations()
        }
    }

    suspend fun getFlightTripsSearchResults(url: String) = safeApiCalls {
        api.getFlighTripsSearchResults(url)
    }

    suspend fun promotionalOffersList() = safeApiCalls {
        api.promotionalOffersList()
    }
}