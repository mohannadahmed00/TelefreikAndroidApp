package com.teleferik.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.R
import com.teleferik.data.Transport
import com.teleferik.databinding.RowTransportationTypeBinding
import com.teleferik.utils.invisibleView




class TransportationTypeAdapter(
    var list: MutableList<Transport>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<TransportationTypeAdapter.ViewHolder>() {


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

            viewBinding.apply {
                if (data.type == "Private"){
                    imgChecked.setImageResource(R.drawable.ic_star)
                    imgChecked.invisibleView(true)
                }else{
                    imgChecked.setImageResource(R.drawable.ic_checked)
                    imgChecked.invisibleView(data.isSelected)
                }



                imgTransport.setImageResource(data.icon)
                tvTransportationName.text = data.name
                if (data.isSelected)
                {
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(imgTransport.drawable),
                        ContextCompat.getColor(imgTransport.context, R.color.base_app_color)
                    )
                    tvTransportationName.setTextColor(ContextCompat.getColor(imgTransport.context, R.color.app_black))
                }else{
                    DrawableCompat.setTint(
                        DrawableCompat.wrap(imgTransport.drawable),
                        ContextCompat.getColor(imgTransport.context, R.color.gray_3)
                    )
                    tvTransportationName.setTextColor(ContextCompat.getColor(imgTransport.context, R.color.gray_3))
                }

                root.setOnClickListener {
                    iClick.onCategoryClicked(data,bindingAdapterPosition,list)
                }
            }
        }
    }


    interface OnItemClickListener {
        fun onCategoryClicked(item: Transport,pos:Int,list:MutableList<Transport>)
    }
}