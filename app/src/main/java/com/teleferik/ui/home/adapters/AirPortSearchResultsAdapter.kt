package com.teleferik.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowAirPortsSearchResultsBinding
import com.teleferik.databinding.RowNotificationsBinding
import com.teleferik.models.skyscanner.airPorts.Place

class AirPortSearchResultsAdapter(
    var list: MutableList<Place>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<AirPortSearchResultsAdapter.ViewHolder>() {


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
        fun bind(data: Place) {
            viewBinding.apply {
                tvName.text = data.placeName
                root.setOnClickListener {
                    iClick.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Place)
    }
}