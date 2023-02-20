package com.teleferik.ui.searchResults.sky_scanner_trip_details

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.WebViewActivity
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSkyScannerTripDetailsBinding
import com.teleferik.models.skyscanner.searchResults.Itinerary
import com.teleferik.models.skyscanner.searchResults.Leg
import com.teleferik.models.skyscanner.searchResults.PricingOption
import com.teleferik.models.skyscanner.searchResults.SearchResultsResponse
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.searchResults.SearchResultsFragmentDirections
import com.teleferik.ui.searchResults.adapter.PricingOptionsAdapter
import com.teleferik.utils.Constants
import com.teleferik.utils.loadImage
import com.teleferik.utils.showHideView
import com.teleferik.utils.showTopToast
import java.text.SimpleDateFormat
import java.util.*


class SkyScannerTripDetailsFragment :
    BaseFragment<HomeViewModel, FragmentSkyScannerTripDetailsBinding, HomeRepo>(),
    PricingOptionsAdapter.OnItemClickListener {
    private var tripData: Itinerary? = null
    private var searchData: SearchResultsResponse? = null
    lateinit var mPricingOptionsAdapter: PricingOptionsAdapter
    val args: SkyScannerTripDetailsFragmentArgs by navArgs()
    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSkyScannerTripDetailsBinding {
        return FragmentSkyScannerTripDetailsBinding.inflate(layoutInflater)
    }

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        getDetailsData()
        initPricingOptionsData()
        initClicks()
        binding.apply {
            val outBoundLeg = searchData?.legs?.let {
                it.find { item -> tripData?.outboundLegId == item.id }
            }
            bindAirPortCompany(outBoundLeg!!, incOutboundTrip.ivTravelCompanyOut, incOutboundTrip.tvCarriersOut)
            bindDestinationStation(outBoundLeg.destinationStation, incOutboundTrip.tvDestinationStationOut)
            bindOriginStation(outBoundLeg.originStation, incOutboundTrip.tvOriginStationOut)
            bindStops(outBoundLeg.stops, incOutboundTrip.tvStopsNumberOut)
            bindDates(outBoundLeg.departure, outBoundLeg.arrival, incOutboundTrip.tvDatesOut)
            bindDuration(outBoundLeg.duration, incOutboundTrip.tvDurationOut)

            val inBoundLeg = searchData?.legs?.let {
                it.find { item -> tripData?.inboundLegId == item.id }
            }
            if (inBoundLeg != null) {
                incInboundTrip.constraintInboundTripHolder.showHideView(true)
                bindAirPortCompany(
                    inBoundLeg,
                    incInboundTrip.ivTravelCompanyIn,
                    incInboundTrip.tvCarriersIn
                )
                bindDestinationStation(
                    inBoundLeg.destinationStation,
                    incInboundTrip.tvDestinationStationIn
                )
                bindOriginStation(inBoundLeg.originStation, incInboundTrip.tvOriginStationIn)
                bindStops(inBoundLeg.stops, incInboundTrip.tvStopsNumberIn)
                bindDates(inBoundLeg.departure, inBoundLeg.arrival, incInboundTrip.tvDatesIn)
                bindDuration(inBoundLeg.duration, incInboundTrip.tvDurationIn)

            } else incInboundTrip.constraintInboundTripHolder.showHideView(false)

        }

    }

    private fun initClicks() {
        binding.imgBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initPricingOptionsData() {
        tripData?.let {
            mPricingOptionsAdapter = PricingOptionsAdapter(
                it.pricingOptions as MutableList<PricingOption>,
                this,
                searchData
            )
            binding.rvPricingOptions.adapter = mPricingOptionsAdapter
        }

    }

    private fun getDetailsData() {
        searchData = args.searchData
        tripData = args.tripData
    }


    private fun bindDuration(duration: Int?, tvDuration: TextView) {
        tvDuration.text = timeConvert(duration!!, tvDuration.context)
    }

    private fun bindDates(departure: String?, arrival: String?, tvDates: TextView) {
        tvDates.text =
            getString(R.string.from_date_to_date, convertDate(departure), convertDate(arrival))
    }


    private fun bindStops(stops: List<Int>?, tvStopsNumber: TextView) {
        if (stops.isNullOrEmpty()) {
            tvStopsNumber.showHideView(true)
        } else {
            if (stops.size == 1)
                tvStopsNumber.text =
                    getString(R.string.one_stop)
            else
                tvStopsNumber.text =
                    getString(R.string.stops_Number, stops.size.toString())
        }
    }

    private fun bindOriginStation(originStation: Int?, tvOriginStation: TextView) {
        tvOriginStation.text = searchData?.places?.find { originStation == it.id }?.name
    }

    private fun bindDestinationStation(
        destinationStation: Int?,
        tvDestinationStation: TextView
    ) {
        tvDestinationStation.text = searchData?.places?.find { destinationStation == it.id }?.name
    }

    private fun bindAirPortCompany(data: Leg, ivTravelCompany: ImageView, tvCarriers: TextView) {
        data.carriers?.let {
            if (it.isNotEmpty()) {
                if (it.size == 1) {
                    val company =
                        searchData?.carriers?.find { company -> company.id == it.first() }
                    ivTravelCompany.loadImage(company?.imageUrl)
                    tvCarriers.showHideView(true)
                } else {
                    ivTravelCompany.visibility = View.INVISIBLE
                    var company = ""
                    data.carriers.forEach { carrierId ->
                        company += "+" + searchData?.carriers?.find { company -> company.id == carrierId }?.name
                    }
                    tvCarriers.text = company

                }
            }
        }
    }

    override fun onPriceOptionClicked(item: PricingOption) {
        if (item.deeplinkUrl != null) {
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra(
                Constants.PARAMS.SCREEN_TITLE,
                getString(R.string.app_name)
            )
            intent.putExtra(Constants.PARAMS.SCREEN_URL,item.deeplinkUrl)
            startActivity(intent)
            /*findNavController().navigate(
                SkyScannerTripDetailsFragmentDirections.actionSkyScannerTripDetailsFragmentToSeatConfirmationFragment(
                    searchData,
                    tripData,
                    item
                )
            )*/
        }else showTopToast(getString(R.string.cant_open_payment))
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

    /*fun timeConvert(time: Int, context: Context): String? {
        var t = ""
        val days = time / (24 * 60)
        val hours = time % (24 * 60) / 60
        val min = time % (24 * 60) % 60
        t = "$days:$hours:$min"
        return context.getString(R.string.trip_duration, "${days.toString()}", "${hours.toString()}", "${min.toString()}")


    }*/
    fun timeConvert(time: Int,context: Context): String? {
        var t = ""
        //val days = time / (24 * 60)
        val hours = time % (24 * 60) / 60
        val min = time % (24 * 60) % 60
        return context.getString(R.string.trip_duration,"${hours.toString()}","${min.toString()}")
    }

}
