package com.teleferik.ui.searchResults

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSearchResultsBinding
import com.teleferik.models.bus.searchResults.TripsSearchResponseItem
import com.teleferik.models.skyscanner.searchResults.Itinerary
import com.teleferik.models.skyscanner.searchResults.SearchResultsResponse
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.searchResults.adapter.BusSearchResultsAdapter
import com.teleferik.ui.searchResults.adapter.FlightSearchResultsAdapter
import com.teleferik.utils.handleApiErrors
import com.teleferik.utils.showHideView


class SearchResultsFragment : BaseFragment<HomeViewModel, FragmentSearchResultsBinding, HomeRepo>(),
<<<<<<< HEAD
    SearchResultsAdapter.OnItemClickListener {
    lateinit var mSearchResultsAdapter: SearchResultsAdapter
=======
    FlightSearchResultsAdapter.OnItemClickListener,BusSearchResultsAdapter.OnItemClickListener {
    private lateinit var mFlightSearchResultsAdapter: FlightSearchResultsAdapter
    private lateinit var mBusSearchResultsAdapter: BusSearchResultsAdapter

>>>>>>> tmp
    private val args: SearchResultsFragmentArgs by navArgs()
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSearchResultsBinding {
        return FragmentSearchResultsBinding.inflate(layoutInflater)
    }

    override fun handleView() {
        initClicks()
        binding.btnNoSearchResults.setOnClickListener { findNavController().navigateUp() }
        callSearchResultsRequest()
    }

    private fun callSearchResultsRequest() {
<<<<<<< HEAD
        //if (flight)
        mViewModel.getTripsSearchResults(args.searchUrl + "?apikey=prtl6749387986743898559646983194&sortType=price&sortOrder=asc")
=======
        if(args.searchUrl!=null) {
            mViewModel.getFlightTripsSearchResults(args.searchUrl + "?apikey=prtl6749387986743898559646983194&sortType=price&sortOrder=asc")
        }else if (args.cityFrom!=null && args.cityTo!=null && args.date!=null){
            Log.e("BusSearchTripsResponse2","${args.cityFrom}--${args.cityTo}--${args.date}")
            mViewModel.getBusTripsSearchResults(args.cityFrom.toString(), args.cityTo.toString(),args.date!!)
        }
>>>>>>> tmp
        observeSearchResults()
    }

    private fun observeSearchResults() {
<<<<<<< HEAD
        mViewModel.tripsSearchResultsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    initRecycler(it.value.itineraries, it.value)
                    mViewModel._tripsSearchResultsResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    handleApiErrors(it)
                    mViewModel._tripsSearchResultsResponse.value = null
=======
        if (args.categoryType == "Flight"){
            mViewModel.tripsFlightSearchResultsResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        loading.cancel()
                        initFlightRecycler(it.value.itineraries, it.value)
                        mViewModel._tripsFlightSearchResultsResponse.value = null
                    }
                    is Resource.Failure -> {
                        loading.cancel()
                        handleApiErrors(it)
                        mViewModel._tripsFlightSearchResultsResponse.value = null
                    }
                    is Resource.Loading -> {
                        loading.show()
                    }
                    else -> {}
                }
            }
        }
        else{
            mViewModel.tripsBusSearchResultsResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        loading.cancel()
                        Log.e("BusSearchTripsResponse3","${it.value.data}")
                        initBustRecycler(it.value.data)
                        mViewModel._tripsFlightSearchResultsResponse.value = null
                    }
                    is Resource.Failure -> {
                        Log.e("BusSearchTripsResponse4","${it.errorCode}--${it.errorBody}")
                        loading.cancel()
                        handleApiErrors(it)
                        mViewModel._tripsFlightSearchResultsResponse.value = null
                    }
                    is Resource.Loading -> {
                        loading.show()
                    }
                    else -> {}
>>>>>>> tmp
                }
            }
        }
    }

    private fun initClicks() {
        binding.imgBack.setOnClickListener { findNavController().navigateUp() }
    }

<<<<<<< HEAD
    private fun initRecycler(itinerary: List<Itinerary>?, value: SearchResultsResponse) {
=======
    private fun initFlightRecycler(itinerary: List<Itinerary>?, value: FlightSearchResultsResponse) {
>>>>>>> tmp
        if (!itinerary.isNullOrEmpty()) {
            binding.rvSearchResults.showHideView(true)
            binding.noSearchResults.showHideView(false)
            mFlightSearchResultsAdapter =
                FlightSearchResultsAdapter(itinerary as MutableList<Itinerary>, value, this)
            binding.rvSearchResults.adapter = mFlightSearchResultsAdapter
        } else {
            binding.rvSearchResults.showHideView(false)
            binding.noSearchResults.showHideView(true)
        }
    }

<<<<<<< HEAD
    override fun onItemClicked(item: Itinerary, searchData: SearchResultsResponse) {
=======
    private fun initBustRecycler(trips: List<TripsSearchResponseItem>?) {
        if (!trips.isNullOrEmpty()) {
            binding.rvSearchResults.showHideView(true)
            binding.noSearchResults.showHideView(false)
            mBusSearchResultsAdapter =
                BusSearchResultsAdapter(trips as MutableList<TripsSearchResponseItem>,args.cityFromName.toString(),args.cityToName.toString(), this)
            binding.rvSearchResults.adapter = mBusSearchResultsAdapter
        } else {
            binding.rvSearchResults.showHideView(false)
            binding.noSearchResults.showHideView(true)
        }
    }


    override fun onItemClicked(item: Itinerary, searchData: FlightSearchResultsResponse) {
>>>>>>> tmp
        findNavController().navigate(
            SearchResultsFragmentDirections.actionSearchResultsFragmentToSkyScannerTripDetailsFragment(
                searchData,
                item
            )
        )

    }

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun onItemClicked() {
        Toast.makeText(context,getString(R.string.soon),Toast.LENGTH_LONG).show()
    }

}