package com.teleferik.ui.seatConfirmation

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teleferik.AppController
import com.teleferik.R

import com.teleferik.base.BaseFragment
import com.teleferik.data.Transport
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSeatConfirmationBinding
import com.teleferik.models.skyscanner.searchResults.Itinerary
import com.teleferik.models.skyscanner.searchResults.PricingOption
import com.teleferik.models.skyscanner.searchResults.SearchResultsResponse

import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.home.adapters.TransportationTypeAdapter
import com.teleferik.ui.searchResults.SearchResultsFragmentArgs
import com.teleferik.utils.showHideView
import java.text.SimpleDateFormat
import java.util.*


class SeatConfirmationFragment :
    BaseFragment<HomeViewModel, FragmentSeatConfirmationBinding, HomeRepo>(){

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentSeatConfirmationBinding.inflate(layoutInflater)

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        initClicks()
    }
    private fun initClicks() {
        //why?
        //binding.includeTicket.tvRemainingTime.visibility = View.GONE
        binding.includeTopBar.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}