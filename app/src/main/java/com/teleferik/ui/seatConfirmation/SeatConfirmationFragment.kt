package com.teleferik.ui.seatConfirmation

import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    private var tripData: Itinerary? = null
    private var searchData: SearchResultsResponse? = null
    private var priceOption: PricingOption? = null

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
        /*getDetailsData()
        binding.apply {
            val outBoundLeg = searchData?.legs?.let {
                it.find { item -> tripData?.outboundLegId == item.id }
            }
            bindDates(outBoundLeg?.departure,outBoundLeg?.arrival,includeTicket.tvDate)
            val inBoundLeg = searchData?.legs?.let {
                it.find { item -> tripData?.inboundLegId == item.id }
            }
        }*/
    }
    private fun bindDates(departure: String?, arrival: String?, tvDates: TextView) {
        /*tvDates.text =
            getString(R.string.from_date_to_date, convertDate(departure), convertDate(arrival))*/
    }

    /*override fun onCategoryClicked(item: Transport, pos: Int, list: MutableList<Transport>) {
        TODO("Not yet implemented")
    }*/

    private fun getDetailsData() {

    }
    private fun convertDate(dateToConvert: String?): String {
        //2021-12-21T08:30:00
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale(AppController.localeManager?.language)
        )
        val outputFormat = SimpleDateFormat(
            "yyyy MMM dd hh:mm a",
            Locale(AppController.localeManager?.language)
        )
        outputFormat.timeZone = TimeZone.getDefault()
        val parsedDate: Date = inputFormat.parse(dateToConvert)
        val formattedDate: String = outputFormat.format(parsedDate)
        return formattedDate
    }
}