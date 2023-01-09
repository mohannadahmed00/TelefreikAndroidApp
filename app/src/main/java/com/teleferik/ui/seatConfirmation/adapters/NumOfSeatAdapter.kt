package com.teleferik.ui.seatConfirmation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.data.Transport
import com.teleferik.databinding.RowTransportationTypeBinding




class NumOfSeatAdapter(
    var list: MutableList<Transport>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<NumOfSeatAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowTransportationTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    //ic_premium
    inner class ViewHolder(var viewBinding: RowTransportationTypeBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: Transport) {

        }
    }


    interface OnItemClickListener {
        fun onCategoryClicked(item: Transport,pos:Int,list:MutableList<Transport>)
    }
}