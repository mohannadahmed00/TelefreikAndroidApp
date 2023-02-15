package com.teleferik.ui.home.searchCities

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hmlabs.kheirzaman.utils.localizationUtils.LocaleManager
import com.teleferik.AppController
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSearchCitiesBinding
import com.teleferik.models.webus.locations.LocationResponseItem
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.home.adapters.LocationSearchResultsAdapter
import com.teleferik.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchCitiesFragment :
    BaseFragment<HomeViewModel, FragmentSearchCitiesBinding, HomeRepo>(),
    LocationSearchResultsAdapter.OnItemClickListener {
    private val args: SearchCitiesFragmentArgs by navArgs()
    lateinit var mLocationSearchResultsAdapter: LocationSearchResultsAdapter
    private var typingJob: Job? = null
    lateinit var text:String
    var lang:String?=null
    var searchLang :String? =null

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentSearchCitiesBinding.inflate(layoutInflater)

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        binding.edtSearch.requestFocus()
        binding.edtSearch.postDelayed(Runnable {
            val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            keyboard!!.showSoftInput(binding.edtSearch, 0)
        }, 200)
        initClicks()
        onKeySearchClicked()
        onSearchTextChanged()
        bindSearchKeyFromHomeScreen()
    }

    private fun bindSearchKeyFromHomeScreen() {
        if (args.searchKey != "-")
            binding.edtSearch.setText(args.searchKey)
    }

    private fun onSearchTextChanged() {
        binding.edtSearch.afterTextChanged {
            if (it.isNotEmpty()) {
                typingJob?.cancel()
                typingJob = lifecycleScope.launch {
                    delay(900)
                    lang = if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC) "ar-AE" else "en-UK"
                    mViewModel.searchLocations()
                    observeLocations()
                }
            }else if (it.isEmpty()){
                binding.rvPlaces.showHideView(false)
            }
        }
    }

    private fun initClicks() {
        binding.imgBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun onKeySearchClicked() {
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                typingJob?.cancel()
                lang =
                    if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC) "ar-AE" else "en-UK"

                mViewModel.searchLocations()//Constants.END_POTINS.CITIES_SEARCH
                observeLocations()
            }


            false
        }
    }

    private fun observeLocations() {
        mViewModel.locationsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.e("LocationResponseSuccess",it.value.toString())
                    binding.progress.showHideView(false)
                    initStartRv(it.value.data)
                    mViewModel._locationsResponse.value = null

                }
                is Resource.Failure -> {
                    Log.e("LocationResponseFailure","${it.errorCode}-->${it.errorBody}")
                    binding.progress.showHideView(false)
                    loading.cancel()
                    handleApiErrors(it)
                    mViewModel._locationsResponse.value = null
                }
                is Resource.Loading -> {
                    binding.progress.showHideView(true)
                }
            }
        }
    }

    private fun initStartRv(value: List<LocationResponseItem>?) {
        val locations = value?.filter { it.name.contains(binding.edtSearch.text,true)} as MutableList
        mLocationSearchResultsAdapter = LocationSearchResultsAdapter(locations, this,searchLang)
        binding.rvPlaces.adapter = mLocationSearchResultsAdapter
        binding.rvPlaces.showHideView(true)

    }

    override fun onItemClicked(item: LocationResponseItem) {
        val data = mutableMapOf<String,Any>()
        data["item"] = item
        if (args.isSearchFromStart)
            setFragmentResult(Constants.START_DESTINATION, bundleOf(Constants.START_DESTINATION to data))
        else
            setFragmentResult(Constants.ARRIVAL_DESTINATION, bundleOf(Constants.ARRIVAL_DESTINATION to data))
        binding.edtSearch.postDelayed(Runnable {
            val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            keyboard!!.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
            findNavController().navigateUp()
        }, 200)
    }


}