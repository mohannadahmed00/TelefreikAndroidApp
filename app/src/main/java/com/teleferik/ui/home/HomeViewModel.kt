package com.teleferik.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleferik.data.network.Resource
import com.teleferik.models.BaseResponse
import com.teleferik.models.promotionalOffer.PromotionalOffer
import com.teleferik.models.skyscanner.airPorts.AirPortsResponse
import com.teleferik.models.skyscanner.searchResults.BookingDetailsLink
import com.teleferik.models.skyscanner.searchResults.Itinerary
import com.teleferik.models.skyscanner.searchResults.SearchResultsResponse
import com.teleferik.models.webus.cities.CitiesResponse
import kotlinx.coroutines.launch


class HomeViewModel(private val homeRepo: HomeRepo) : ViewModel() {
    val list = listOf<Itinerary>(
        Itinerary(BookingDetailsLink("", "", ""), "", "", emptyList()),
        Itinerary(BookingDetailsLink("", "", ""), "", "", emptyList()),
        Itinerary(BookingDetailsLink("", "", ""), "", "", emptyList()),
        Itinerary(BookingDetailsLink("", "", ""), "", "", emptyList()),
        Itinerary(BookingDetailsLink("", "", ""), "", "", emptyList()),
        Itinerary(BookingDetailsLink("", "", ""), "", "", emptyList()),
    )

    private var tripType = 1  // 0 go only // 1 go and return
    val _airPortsResponse = MutableLiveData<Resource<AirPortsResponse>>()
    val airPortsResponse: LiveData<Resource<AirPortsResponse>> get() = _airPortsResponse

    val _citiesResponse = MutableLiveData<Resource<CitiesResponse>>()
    val citiesResponse: LiveData<Resource<CitiesResponse>> get() = _citiesResponse

    val _tripsSearchResultsResponse = MutableLiveData<Resource<SearchResultsResponse>>()
    val tripsSearchResultsResponse: LiveData<Resource<SearchResultsResponse>> get() = _tripsSearchResultsResponse

    val _promotionalOffersList = MutableLiveData<Resource<BaseResponse<PromotionalOffer>>>()
    val promotionalOffersList: LiveData<Resource<BaseResponse<PromotionalOffer>>> get() = _promotionalOffersList

    fun searchAirPorts(url: String) = viewModelScope.launch {
        _airPortsResponse.value = Resource.Loading
        _airPortsResponse.value = homeRepo.searchAirPorts(url)
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
}