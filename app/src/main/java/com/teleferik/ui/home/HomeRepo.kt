package com.teleferik.ui.home


import com.teleferik.base.BaseRepo
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.models.BaseResponse
import com.teleferik.models.bus.locations.LocationsResponseItem
import com.teleferik.models.bus.searchResults.TripsSearchResponseItem


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

    suspend fun getBusTripsSearchResults(city_from:String,city_to:String,date:String):Resource<BaseResponse<List<TripsSearchResponseItem>>> {
        return safeApiCalls {
            //api.getBusTripsSearchResults("7", "2", "2023-03-15")//,"2","3"
            api.getBusTripsSearchResults(city_from, city_to, date)//,"2","3"

        }
    }

    suspend fun promotionalOffersList() = safeApiCalls {
        api.promotionalOffersList()
    }
}