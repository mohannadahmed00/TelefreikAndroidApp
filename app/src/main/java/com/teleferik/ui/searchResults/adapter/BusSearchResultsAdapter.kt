package com.teleferik.ui.searchResults.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.databinding.RowSearchResultsBinding
import com.teleferik.models.bus.searchResults.StationsFrom
import com.teleferik.models.bus.searchResults.StationsTo
import com.teleferik.models.bus.searchResults.TripsSearchResponseItem
import com.teleferik.models.skyscanner.searchResults.Itinerary

import com.teleferik.models.skyscanner.searchResults.Leg
import com.teleferik.models.skyscanner.searchResults.FlightSearchResultsResponse
import com.teleferik.utils.loadImage
import com.teleferik.utils.showHideView
import java.text.SimpleDateFormat
import java.util.*



class BusSearchResultsAdapter(
    var list: MutableList<TripsSearchResponseItem>,
    val cityFrom:String,
    val cityTo:String,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<BusSearchResultsAdapter.ViewHolder>() {


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
        fun bind(data: TripsSearchResponseItem) {


            viewBinding.apply {
                viewBinding.incInboundTrip.constraintInboundTripHolder.showHideView(false)

                bindTransImage(R.drawable.bus, incOutboundTrip.ivTransOut)
                //Log.e("FromResponse",x.toString())
                bindStationOut(cityFrom,incOutboundTrip.tvOriginStationOut)
                //val y = data.cities_to.filter { it.id == cityTo }
                //Log.e("ToResponse",y.toString())
                bindStationIn(cityTo,incOutboundTrip.tvDestinationStationOut)
                bindStops(data.stations_from,data.stations_to, incOutboundTrip.tvStopsNumberOut)
                bindClassCategory(data.bus.category,incOutboundTrip.appCompatButtonOut)
                bindCompanyName(data.gateway_id, incOutboundTrip.tvCarriersOut)
                bindDates(data.time,data.date,incOutboundTrip.tvDatesOut)
                val station = data.stations_from.filter { it.name.contains(cityFrom,true) }.single()
                bindPrice(station.price,tvPrice)


                incOutboundTrip.ivDirTransOut.setImageResource(R.drawable.ic_dir_bus)
                DrawableCompat.setTint(
                    DrawableCompat.wrap(incOutboundTrip.ivDirTransOut.drawable),
                    ContextCompat.getColor(incOutboundTrip.ivDirTransOut.context, R.color.base_app_color)
                )
                //tvDatesOut.visibility = View.GONE
                incOutboundTrip.tvDurationOut.visibility = View.GONE
                incOutboundTrip.ivTravelCompanyOut.visibility = View.INVISIBLE

                /*bindAirPortCompany(inBoundLeg, ivTravelCompanyIn, tvCarriersIn)
                bindDestinationStation(inBoundLeg.destinationStation, tvDestinationStationIn)
                bindOriginStation(inBoundLeg.originStation, tvOriginStationIn)

                bindDuration(inBoundLeg.duration, tvDurationIn)*/


            }

            viewBinding.btnChoose.setOnClickListener {
                iClick.onItemClicked()//todo work on it
            }

        }

        private fun bindTransImage(src: Int, ivTrans: ImageView) {
            ivTrans.setImageResource(src)
        }

        private fun bindStationOut(fromStation: String, tvStationOut: TextView) {
            tvStationOut.text = fromStation
        }

        private fun bindStationIn(toStation: String, tvStationTo: TextView) {
            tvStationTo.text = toStation
        }

        private fun bindStops(fromStops: List<StationsFrom>?, toStops: List<StationsTo>?, tvStopsNumber: TextView) {
            if (fromStops.isNullOrEmpty() && toStops.isNullOrEmpty()) {
                tvStopsNumber.showHideView(true)
            } else {
                val size = toStops?.size?.let { fromStops?.size?.plus(it) }
                if ( size  == 1)
                    tvStopsNumber.text =
                        viewBinding.root.context.getString(R.string.one_stop)
                else
                    tvStopsNumber.text =
                        viewBinding.root.context.getString(
                            R.string.stops_Number,
                            size.toString()
                        )
            }
        }

        private fun bindClassCategory(categoryClass:String,btnClass: Button){
            btnClass.text = categoryClass
        }

        private fun bindCompanyName(companyName: String, tvCompanyName: TextView) {
            tvCompanyName.text = companyName
        }


        private fun bindDates(time: String, date: String, tvDates: TextView) {
            tvDates.text = "$time $date"
        }


        private fun bindPrice(price:String,tvPrice:TextView){
            tvPrice.text = "$price EGP"
        }













        /*private fun bindPriceOfferCounts(size: Int?) {
            size?.let {
                viewBinding.tvPriceOffers.text =
                    viewBinding.root.context.getString(R.string.price_offers, it.toString())
            }
        }
        private fun bindDuration(duration: Int?, tvDuration: TextView) {
            tvDuration.text = timeConvert(duration!!, tvDuration.context)
        }
        private fun bindDates(departure: String?, arrival: String?, tvDates: TextView) {
            tvDates.text = viewBinding.root.context.getString(
                R.string.from_date_to_date,
                convertDate(departure),
                convertDate(arrival)
            )
        }
        private fun bindStartPrice(price: Double?, tvPrice: TextView) {
            *//*price?.let {
                tvPrice.text = viewBinding.root.context.getString(
                    R.string.from_price,
                    it.toString(),
                    searchData.query?.currency
                )
            }*//*
        }
*/







    }

    /*private fun convertDate(dateToConvert: String?): String {
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
    fun timeConvert(time: Int, context: Context): String? {
        var t = ""
        //val days = time / (24 * 60)
        val hours = time % (24 * 60) / 60
        val min = time % (24 * 60) % 60
        return context.getString(R.string.trip_duration, "${hours.toString()}", "${min.toString()}")
    }*/

    interface OnItemClickListener {
        fun onItemClicked()
    }

}