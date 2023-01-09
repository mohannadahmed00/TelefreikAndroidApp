package com.teleferik.ui.dashboard.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.R
import com.teleferik.databinding.RowReservationsBinding
import com.teleferik.models.ticketReservation.SingleTicketReservation

class DashboardAdapter(
    var context:Context,
    var list: MutableList<SingleTicketReservation>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowReservationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(var viewBinding: RowReservationsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: SingleTicketReservation) {
            viewBinding.apply {
                this.tvDate.text = data.date
                this.tvPrice.text = data.price.toString()
                this.tvDeb.text = data.departure
                this.tvArr.text = data.arrival
                this.type.text = when(data.type){
                    "Flights" -> {
                        context.getString(R.string.plan)
                    }
                    "Voyage" -> {
                        context.getString(R.string.voyage)
                    }
                    "Trains" -> {
                        context.getString(R.string.train)
                    }
                    "Buses" -> {
                        context.getString(R.string.bus)
                    }
                    "Microbuses" -> {
                        context.getString(R.string.minibus)
                    }
                    "Limousine" -> {
                        context.getString(R.string.limousine)
                    }
                    else -> {
                        context.getString(R.string.plan)
                    }
                }
                when(data.type){
                    "Flights" -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_flights))
                    }
                    "Voyage" -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_voyage))
                    }
                    "Trains" -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_trains))
                    }
                    "Buses" -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_buses))
                    }
                    "Microbuses" -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_minibuses))
                    }
                    "Limousine" -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_limousine))
                    }
                    else -> {
                        this.type.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.dashboard_chip_bg))
                    }
                }
                root.setOnClickListener {
                    iClick.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: SingleTicketReservation)
    }

    fun clear() {
        list.clear()
        notifyItemRangeRemoved(0, list.size)
    }
}