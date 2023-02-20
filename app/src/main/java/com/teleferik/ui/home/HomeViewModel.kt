package com.teleferik.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleferik.data.network.Resource
import com.teleferik.models.BaseResponse
import com.teleferik.models.promotionalOffer.PromotionalOffer
import com.teleferik.models.skyscanner.airPorts.AirPortsResponse
import com.teleferik.models.skyscanner.searchResults.FlightSearchResultsResponse
import com.teleferik.models.bus.locations.LocationsResponseItem
import com.teleferik.models.bus.searchResults.TripsSearchResponseItem
import kotlinx.coroutines.launch


class HomeViewModel(private val homeRepo: HomeRepo) : ViewModel() {


    private var tripType = 1  // 0 go only // 1 go and return
    val _airPortsResponse = MutableLiveData<Resource<AirPortsResponse>>()
    val airPortsResponse: LiveData<Resource<AirPortsResponse>> get() = _airPortsResponse

    val _locationsResponse = MutableLiveData<Resource<BaseResponse<List<LocationsResponseItem>>>>()
    val locationsResponse: LiveData<Resource<BaseResponse<List<LocationsResponseItem>>>> get() = _locationsResponse


    val _tripsFlightSearchResultsResponse = MutableLiveData<Resource<FlightSearchResultsResponse>>()
    val tripsFlightSearchResultsResponse: LiveData<Resource<FlightSearchResultsResponse>> get() = _tripsFlightSearchResultsResponse

    val _tripsBusSearchResultsResponse = MutableLiveData<Resource<BaseResponse<List<TripsSearchResponseItem>>>>()
    val tripsBusSearchResultsResponse: LiveData<Resource<BaseResponse<List<TripsSearchResponseItem>>>> get() = _tripsBusSearchResultsResponse

    val _promotionalOffersList = MutableLiveData<Resource<BaseResponse<PromotionalOffer>>>()
    val promotionalOffersList: LiveData<Resource<BaseResponse<PromotionalOffer>>> get() = _promotionalOffersList

    fun searchAirPorts(url: String) = viewModelScope.launch {
        _airPortsResponse.value = Resource.Loading
        _airPortsResponse.value = homeRepo.searchAirPorts(url)
    }

    fun searchLocations() = viewModelScope.launch {
        _locationsResponse.value = Resource.Loading
        _locationsResponse.value = homeRepo.searchLocations()
    }


    fun getFlightTripsSearchResults(url: String) = viewModelScope.launch {
        _tripsFlightSearchResultsResponse.value = Resource.Loading
        _tripsFlightSearchResultsResponse.value = homeRepo.getFlightTripsSearchResults(url)/*Resource.Success(SearchResultsResponse(null,null,null,list,null,null,null,null,null,null))*///
    }

    fun getBusTripsSearchResults(city_from: String, city_to:String, date:String) = viewModelScope.launch {
        _tripsBusSearchResultsResponse.value = Resource.Loading
        _tripsBusSearchResultsResponse.value = homeRepo.getBusTripsSearchResults(city_from,city_to,date)/*Resource.Success(SearchResultsResponse(null,null,null,list,null,null,null,null,null,null))*///
    }

    fun promotionalOffersList() = viewModelScope.launch {
        _promotionalOffersList.value = Resource.Loading
        _promotionalOffersList.value = homeRepo.promotionalOffersList()
    }

    fun getTripType(): Int {
        return tripType
    }

     fun setTripType(type: Int) {
        tripType = type
    }
}