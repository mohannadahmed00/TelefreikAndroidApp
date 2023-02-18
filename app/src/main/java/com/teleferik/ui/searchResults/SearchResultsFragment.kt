package com.teleferik.ui.searchResults

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSearchResultsBinding
import com.teleferik.models.skyscanner.searchResults.Itinerary
import com.teleferik.models.skyscanner.searchResults.FlightSearchResultsResponse
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.searchResults.adapter.SearchResultsAdapter
import com.teleferik.utils.handleApiErrors
import com.teleferik.utils.showHideView


class SearchResultsFragment : BaseFragment<HomeViewModel, FragmentSearchResultsBinding, HomeRepo>(),
    SearchResultsAdapter.OnItemClickListener {
    private lateinit var mSearchResultsAdapter: SearchResultsAdapter
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
        if(args.searchUrl!=null) {
            mViewModel.getFlightTripsSearchResults(args.searchUrl + "?apikey=prtl6749387986743898559646983194&sortType=price&sortOrder=asc")
            observeSearchResults()
        }else{
            //todo start search bus trips from here
        }
    }

    private fun observeSearchResults() {
        mViewModel.tripsFlightSearchResultsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    initRecycler(it.value.itineraries, it.value)
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

    private fun initClicks() {
        binding.imgBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initRecycler(itinerary: List<Itinerary>?, value: FlightSearchResultsResponse) {
        if (!itinerary.isNullOrEmpty()) {
            binding.rvSearchResults.showHideView(true)
            binding.noSearchResults.showHideView(false)
            mSearchResultsAdapter =
                SearchResultsAdapter(itinerary as MutableList<Itinerary>, value, this)
            binding.rvSearchResults.adapter = mSearchResultsAdapter
        } else {
            binding.rvSearchResults.showHideView(false)
            binding.noSearchResults.showHideView(true)
        }
    }

    override fun onItemClicked(item: Itinerary, searchData: FlightSearchResultsResponse) {
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

}