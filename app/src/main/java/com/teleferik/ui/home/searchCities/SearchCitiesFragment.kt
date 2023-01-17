package com.teleferik.ui.home.searchCities

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.ui.input.pointer.PointerIconDefaults.Text
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.core.content.ContextCompat.getSystemService
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
import com.teleferik.databinding.FragmentSearchAriPortsBinding
import com.teleferik.databinding.FragmentSearchCitiesBinding
import com.teleferik.models.skyscanner.airPorts.AirPortsResponse
import com.teleferik.models.skyscanner.airPorts.Place
import com.teleferik.models.webus.cities.CitiesResponse
import com.teleferik.models.webus.cities.City
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.home.adapters.AirPortSearchResultsAdapter
import com.teleferik.ui.home.adapters.CitiesSearchResultsAdapter
import com.teleferik.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets


class SearchCitiesFragment :
    BaseFragment<HomeViewModel, FragmentSearchCitiesBinding, HomeRepo>(),
    CitiesSearchResultsAdapter.OnItemClickListener {
    private val args: SearchCitiesFragmentArgs by navArgs()
    lateinit var mCitySearchResultsAdapter: CitiesSearchResultsAdapter
    private var typingJob: Job? = null
    lateinit var text:String
    var lang:String?=null

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
                    mViewModel.searchCities(Constants.END_POTINS.CITIES_SEARCH)
                    observeCities()
                }
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

                mViewModel.searchCities(Constants.END_POTINS.CITIES_SEARCH)
                observeCities()
            }


            false
        }
    }

    private fun observeCities() {
        mViewModel.citiesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progress.showHideView(false)
                    initStartRv(it.value)
                    mViewModel._citiesResponse.value = null

                }
                is Resource.Failure -> {
                    binding.progress.showHideView(false)
                    loading.cancel()
                    handleApiErrors(it)
                    mViewModel._citiesResponse.value = null
                }
                is Resource.Loading -> {
                    binding.progress.showHideView(true)
                }
            }
        }
    }

    private fun initStartRv(value: CitiesResponse) {
        /*val imm =  context?.getSystemService(INPUT_METHOD_SERVICE)
        Toast.makeText(context,imm.toString(),Toast.LENGTH_SHORT).show()*/

        val cities = if (lang == "en-UK"){
            value.cities?.filter { it.name.contains(binding.edtSearch.text, ignoreCase = true) } as MutableList
        }else{
            value.cities?.filter { String(it.translations[0].name.toByteArray(),StandardCharsets.UTF_8).contains(binding.edtSearch.text)} as MutableList
        }

        mCitySearchResultsAdapter = CitiesSearchResultsAdapter(cities, this,lang)
        binding.rvPlaces.adapter = mCitySearchResultsAdapter
        binding.rvPlaces.showHideView(true)

    }

    override fun onItemClicked(item: City) {
        if (args.isSearchFromStart)
            setFragmentResult(Constants.START_DESTINATION, bundleOf(Constants.START_DESTINATION to item))
        else
            setFragmentResult(Constants.ARRIVAL_DESTINATION, bundleOf(Constants.ARRIVAL_DESTINATION to item))
        binding.edtSearch.postDelayed(Runnable {
            val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            keyboard!!.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
            findNavController().navigateUp()
        }, 200)
    }


}