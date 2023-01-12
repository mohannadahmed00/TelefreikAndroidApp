package com.teleferik.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowAirPortsSearchResultsBinding
import com.teleferik.databinding.RowNotificationsBinding
import com.teleferik.models.skyscanner.airPorts.Place
import com.teleferik.models.webus.cities.City
import java.nio.charset.StandardCharsets

class CitiesSearchResultsAdapter(var list: MutableList<City>, private val iClick: OnItemClickListener,val lang:String?) : RecyclerView.Adapter<CitiesSearchResultsAdapter.ViewHolder>() {


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
        fun bind(data: City) {
            viewBinding.apply {
                if (lang == "en-UK"){
                    tvName.text = data.name
                }else{
                    tvName.text = String(data.translations[0].name.encodeToByteArray(),StandardCharsets.UTF_8)
                }

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