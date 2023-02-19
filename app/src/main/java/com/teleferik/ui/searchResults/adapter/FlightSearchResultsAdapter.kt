package com.teleferik.ui.searchResults.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.databinding.RowSearchResultsBinding
import com.teleferik.models.skyscanner.searchResults.Itinerary

import com.teleferik.models.skyscanner.searchResults.Leg
import com.teleferik.models.skyscanner.searchResults.FlightSearchResultsResponse
import com.teleferik.utils.loadImage
import com.teleferik.utils.showHideView
import java.text.SimpleDateFormat
import java.util.*

class FlightSearchResultsAdapter(
    var list: MutableList<Itinerary>,
    private val searchData: FlightSearchResultsResponse,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<FlightSearchResultsAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowSearchResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(var viewBinding: RowSearchResultsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: Itinerary) {
            viewBinding.apply {
                val outBoundLeg = searchData.legs?.let {
                   it.find { item -> data.outboundLegId == item.id }
                }
                bindAirPortCompany(outBoundLeg!!,incOutboundTrip.ivTravelCompanyOut,incOutboundTrip.tvCarriersOut)
                bindDestinationStation(outBoundLeg.destinationStation,incOutboundTrip.tvDestinationStationOut)
                bindOriginStation(outBoundLeg.originStation,incOutboundTrip.tvOriginStationOut)
                bindStops(outBoundLeg.stops,incOutboundTrip.tvStopsNumberOut)
                bindDates(outBoundLeg.departure,outBoundLeg.arrival,incOutboundTrip.tvDatesOut)
                bindStartPrice(data.pricingOptions?.first()?.price,tvPrice)
                bindDuration(outBoundLeg.duration,incInboundTrip.tvDurationIn)
                bindPriceOfferCounts(data.pricingOptions?.size)

            }


            viewBinding.incInboundTrip.apply {
                val inBoundLeg = searchData.legs?.let {
                    it.find { item -> data.inboundLegId == item.id }
                }
                if (inBoundLeg != null) {
                    viewBinding.incInboundTrip.constraintInboundTripHolder.showHideView(true)
                    bindAirPortCompany(inBoundLeg, ivTravelCompanyIn, tvCarriersIn)
                    bindDestinationStation(inBoundLeg.destinationStation, tvDestinationStationIn)
                    bindOriginStation(inBoundLeg.originStation, tvOriginStationIn)
                    bindStops(inBoundLeg.stops, tvStopsNumberIn)
                    bindDates(inBoundLeg.departure, inBoundLeg.arrival, tvDatesIn)
                    bindDuration(inBoundLeg.duration,tvDurationIn)

                }else viewBinding.incInboundTrip.constraintInboundTripHolder.showHideView(false)
            }

            viewBinding.btnChoose.setOnClickListener {
                iClick.onItemClicked(data,searchData)
            }

        }

        private fun bindPriceOfferCounts(size: Int?) {
            size?.let { viewBinding.tvPriceOffers.text = viewBinding.root.context.getString(R.string.price_offers,it.toString()) }
        }

        private fun bindDuration(duration: Int?, tvDuration: TextView) {
           tvDuration.text = timeConvert(duration!!, tvDuration.context)
        }

        private fun bindDates(departure: String?, arrival: String?, tvDates: TextView) {
            tvDates.text = viewBinding.root.context.getString(R.string.from_date_to_date,convertDate(departure),convertDate(arrival))
        }

        private fun bindStartPrice(price: Double?, tvPrice: TextView) {
            price?.let {
                tvPrice.text = viewBinding.root.context.getString(R.string.from_price,it.toString(),searchData.query?.currency)
            }
        }

        private fun bindStops(stops: List<Int>?, tvStopsNumber: TextView) {
            if (stops.isNullOrEmpty()){
                tvStopsNumber.showHideView(true)
            }else {
                if (stops.size == 1)
                    tvStopsNumber.text =
                        viewBinding.root.context.getString(R.string.one_stop)
                else
                tvStopsNumber.text =
                    viewBinding.root.context.getString(R.string.stops_Number, stops.size.toString())
            }
        }

        private fun bindOriginStation(originStation: Int?, tvOriginStation: TextView) {
            tvOriginStation.text = searchData.places?.find { originStation == it.id }?.name
        }

        private fun bindDestinationStation(destinationStation: Int?, tvDestinationStation: TextView) {
            tvDestinationStation.text = searchData.places?.find { destinationStation == it.id }?.name
        }

        private fun bindAirPortCompany(data: Leg, ivTravelCompany: ImageView, tvCarriers: TextView) {
            data.carriers?.let {
                if (it.isNotEmpty()) {
                    if (it.size == 1) {
                        val company =
                            searchData.carriers?.find { company -> company.id == it.first() }
                        ivTravelCompany.loadImage(company?.imageUrl)
                        tvCarriers.showHideView(true)
                    } else {
                        ivTravelCompany.visibility = View.INVISIBLE
                        var company = ""
                        data.carriers.forEach { carrierId ->
                            company+="+"+searchData.carriers?.find { company -> company.id == carrierId }?.name
                        }
                        tvCarriers.text = company

                    }
                }
            }
        }
    }

    private fun convertDate(dateToConvert: String?): String {
        //2021-12-21T08:30:00
        val inputFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale(AppController.localeManager?.language))
//        val outputFormat =
//            SimpleDateFormat("yyyy MMM dd hh:mm a", Locale(AppController.localeManager?.language))
        val outputFormat =
            SimpleDateFormat("dd MMM hh:mm a", Locale(AppController.localeManager?.language))
        outputFormat.timeZone = TimeZone.getDefault()
        val parsedDate: Date = inputFormat.parse(dateToConvert)
        return outputFormat.format(parsedDate)
    }
    fun timeConvert(time: Int,context: Context): String? {
        var t = ""
        //val days = time / (24 * 60)
        val hours = time % (24 * 60) / 60
        val min = time % (24 * 60) % 60
        return context.getString(R.string.trip_duration,"${hours.toString()}","${min.toString()}")
    }
    interface OnItemClickListener {
        fun onItemClicked(item: Itinerary, searchData: FlightSearchResultsResponse)
    }

}