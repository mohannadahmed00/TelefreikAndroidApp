package com.teleferik.ui.home


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleferik.data.network.Resource
import com.teleferik.models.BaseListResponse
import com.teleferik.models.BaseResponse
import com.teleferik.models.bus.busTrips.BusTrip
import com.teleferik.models.promotionalOffer.PromotionalOffer
import com.teleferik.models.skyscanner.airPorts.AirPortsResponse
<<<<<<< HEAD
import com.teleferik.models.skyscanner.searchResults.FlightSearchResultsResponse
import com.teleferik.models.bus.locations.LocationsResponseItem
import com.teleferik.models.seats.AvailableSeat
=======
import com.teleferik.models.skyscanner.searchResults.BookingDetailsLink
import com.teleferik.models.skyscanner.searchResults.Itinerary
import com.teleferik.models.skyscanner.searchResults.SearchResultsResponse
import com.teleferik.models.webus.cities.CitiesResponse
import com.teleferik.models.webus.locations.LocationResponse
>>>>>>> parent of 8c4015a (everything is ready to work on search trips)
import kotlinx.coroutines.launch


class HomeViewModel(private val homeRepo: HomeRepo) : ViewModel() {


    private var tripType = 1  // 0 go only // 1 go and return
    val _airPortsResponse = MutableLiveData<Resource<AirPortsResponse>>()
    val airPortsResponse: LiveData<Resource<AirPortsResponse>> get() = _airPortsResponse

    val _locationsResponse = MutableLiveData<Resource<BaseResponse<LocationResponse>>>()
    val locationsResponse: LiveData<Resource<BaseResponse<LocationResponse>>> get() = _locationsResponse



    val _citiesResponse = MutableLiveData<Resource<CitiesResponse>>()
    val citiesResponse: LiveData<Resource<CitiesResponse>> get() = _citiesResponse

    val _tripsSearchResultsResponse = MutableLiveData<Resource<SearchResultsResponse>>()
    val tripsSearchResultsResponse: LiveData<Resource<SearchResultsResponse>> get() = _tripsSearchResultsResponse

    val _promotionalOffersList = MutableLiveData<Resource<BaseResponse<PromotionalOffer>>>()
    val promotionalOffersList: LiveData<Resource<BaseResponse<PromotionalOffer>>> get() = _promotionalOffersList

    val _busTripsResponse = MutableLiveData<Resource<BaseListResponse<List<BusTrip>>>>()
    val busTripsResponse : LiveData<Resource<BaseListResponse<List<BusTrip>>>> get() = _busTripsResponse

    val _tripDetailsResponse = MutableLiveData<Resource<BaseListResponse<BusTrip>>>()
    val tripDetailsResponse : LiveData<Resource<BaseListResponse<BusTrip>>> get() = _tripDetailsResponse

    val _availableSeatsResponse = MutableLiveData<Resource<BaseListResponse<List<AvailableSeat>>>>()
    val availableSeatsResponse : LiveData<Resource<BaseListResponse<List<AvailableSeat>>>> get() = _availableSeatsResponse


    fun searchAirPorts(url: String) = viewModelScope.launch {
        _airPortsResponse.value = Resource.Loading
        _airPortsResponse.value = homeRepo.searchAirPorts(url)
    }

    fun searchLocations() = viewModelScope.launch {
        _locationsResponse.value = Resource.Loading
        _locationsResponse.value = homeRepo.searchLocations()
    }

    fun searchCities(url: String) = viewModelScope.launch {
        _citiesResponse.value = Resource.Loading
        _citiesResponse.value = homeRepo.searchCities(url)
    }

    fun getTripsSearchResults(url: String) = viewModelScope.launch {
        _tripsSearchResultsResponse.value = Resource.Loading
        _tripsSearchResultsResponse.value = homeRepo.getTripsSearchResults(url)/*Resource.Success(SearchResultsResponse(null,null,null,list,null,null,null,null,null,null))*///
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

    fun getBusTripsSearch(from:String , to:String , date:String) = viewModelScope.launch {
        _busTripsResponse.value = Resource.Loading
        _busTripsResponse.value = homeRepo.searchBusTrips(from, to, date)
    }

    fun getTripDetails(tripId :String) = viewModelScope.launch {
        _tripDetailsResponse.value = Resource.Loading
        _tripDetailsResponse.value = homeRepo.getBusTripDetails(tripId)
    }

    fun getAvailableSeats(tripId: String) = viewModelScope.launch {
        _availableSeatsResponse.value = Resource.Loading
        _availableSeatsResponse.value = homeRepo.getAvailableSeats(tripId)
    }

}