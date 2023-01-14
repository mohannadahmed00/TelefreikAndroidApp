package com.teleferik.ui.home.searchAirPorts

import android.content.Context
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
import com.teleferik.databinding.FragmentSearchAriPortsBinding
import com.teleferik.models.skyscanner.airPorts.AirPortsResponse
import com.teleferik.models.skyscanner.airPorts.Place
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.home.adapters.AirPortSearchResultsAdapter
import com.teleferik.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchAriPortsFragment :
    BaseFragment<HomeViewModel, FragmentSearchAriPortsBinding, HomeRepo>(),
    AirPortSearchResultsAdapter.OnItemClickListener {
    lateinit var mAirPortSearchResultsAdapter: AirPortSearchResultsAdapter
    private val args: SearchAriPortsFragmentArgs by navArgs()
    private var typingJob: Job? = null

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentSearchAriPortsBinding.inflate(layoutInflater)

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
                    val lang =
                        if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC) "ar-AE" else "en-UK"
                    mViewModel.searchAirPorts(Constants.END_POTINS.SKY_SCANNER_AIR_PORTS_SEARCH + "$lang/?apiKey=prtl6749387986743898559646983194&query=$it")
                    observeAirPorts()
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
                val lang =
                    if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC) "ar-AE" else "en-UK"
                mViewModel.searchAirPorts(Constants.END_POTINS.SKY_SCANNER_AIR_PORTS_SEARCH + "$lang/?apiKey=prtl6749387986743898559646983194&query=${binding.edtSearch.captureText()}")
                observeAirPorts()
            }
            false
        }
    }

    private fun observeAirPorts() {
        mViewModel.airPortsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progress.showHideView(false)
                    initStartRv(it.value)
                    mViewModel._airPortsResponse.value = null

                }
                is Resource.Failure -> {
                    binding.progress.showHideView(false)
                    loading.cancel()
                    handleApiErrors(it)
                    mViewModel._airPortsResponse.value = null
                }
                is Resource.Loading -> {
                    binding.progress.showHideView(true)
                }
            }
        }
    }

    private fun initStartRv(value: AirPortsResponse) {
        mAirPortSearchResultsAdapter = AirPortSearchResultsAdapter(value.places!!, this)
        binding.rvPlaces.adapter = mAirPortSearchResultsAdapter
        binding.rvPlaces.showHideView(true)

    }

    override fun onItemClicked(item: Place) {
        if (args.isSearchFromStart)
            setFragmentResult(
                Constants.START_DESTINATION,
                bundleOf(Constants.START_DESTINATION to item)
            )
        else
            setFragmentResult(
                Constants.ARRIVAL_DESTINATION,
                bundleOf(Constants.ARRIVAL_DESTINATION to item)
            )
        binding.edtSearch.postDelayed(Runnable {
            val keyboard =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            keyboard!!.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
            findNavController().navigateUp()
        }, 200)
    }
}