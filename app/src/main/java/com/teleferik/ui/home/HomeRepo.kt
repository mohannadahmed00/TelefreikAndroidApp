package com.teleferik.ui.home


import com.teleferik.base.BaseRepo
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.models.BaseResponse
import com.teleferik.models.webus.locations.LocationResponse
import com.teleferik.models.webus.locations.LocationResponseItem


class HomeRepo(private val api: ApisService) : BaseRepo() {

    suspend fun searchAirPorts(url: String) = safeApiCalls {
        api.searchAirPorts(url)
    }

    suspend fun searchLocations():Resource<BaseResponse<LocationResponse>> {
        return safeApiCalls {
            api.searchLocations()
        }
    }
    suspend fun searchCities(url: String) = safeApiCalls {
        api.searchCities(url)
    }

    suspend fun getTripsSearchResults(url: String) = safeApiCalls {
        api.getTripsSearchResults(url)
    }

    suspend fun promotionalOffersList() = safeApiCalls {
        api.promotionalOffersList()
    }
}