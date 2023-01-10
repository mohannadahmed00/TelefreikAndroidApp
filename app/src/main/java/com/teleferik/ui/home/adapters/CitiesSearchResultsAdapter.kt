package com.teleferik.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowAirPortsSearchResultsBinding
import com.teleferik.databinding.RowNotificationsBinding
import com.teleferik.models.skyscanner.airPorts.Place
import com.teleferik.models.webus.cities.City

class CitiesSearchResultsAdapter(
    var list: MutableList<City>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<CitiesSearchResultsAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowAirPortsSearchResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(var viewBinding: RowAirPortsSearchResultsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: City) {
            viewBinding.apply {
                tvName.text = data.name
                root.setOnClickListener {
                    iClick.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: City)
    }
}