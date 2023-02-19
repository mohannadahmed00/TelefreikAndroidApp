package com.teleferik.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowAirPortsSearchResultsBinding
import com.teleferik.models.webus.locations.LocationResponseItem

class LocationSearchResultsAdapter(var list:MutableList<LocationResponseItem>, private val iClick: OnItemClickListener, val lang:String?) : RecyclerView.Adapter<LocationSearchResultsAdapter.ViewHolder>() {


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

    /*val x = String(value.cities?.get(0)?.translations?.get(0)?.name!!.encodeToByteArray(),StandardCharsets.UTF_8)
        Toast.makeText(context,x,Toast.LENGTH_LONG).show()*/
    inner class ViewHolder(var viewBinding: RowAirPortsSearchResultsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: LocationResponseItem) {
            viewBinding.apply {
                /*if (lang == "en"){
                    tvName.text = data.translations[1].name
                }else{
                    tvName.text = String(data.translations[0].name.encodeToByteArray(),StandardCharsets.UTF_8)
                }*/

                tvName.text = data.name
                root.setOnClickListener {
                    iClick.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: LocationResponseItem)
    }
}