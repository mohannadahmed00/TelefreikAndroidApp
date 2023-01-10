package com.teleferik.ui.seatSelection.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.R
import com.teleferik.databinding.RowSeatItemBinding
import com.teleferik.models.seats.Seat

class SeatSelectionAdapter(
    var seats: List<Seat>,
    private val iClick: OnItemClickListener
):RecyclerView.Adapter<SeatSelectionAdapter.ViewHolder>(){

    private val secondColPos = listOf(2,6,10,14,18,22,26,30,34,38,42)
    private val thirdColPos = listOf(3,7,11,15,19,23,27,31,35,39,43)


    override fun getItemCount() = seats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder (
            RowSeatItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
                )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(seats[position])
    }


    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.layoutPosition in thirdColPos){
            val layoutParams =  holder.viewBinding.myFrame.layoutParams as ViewGroup.MarginLayoutParams
            /*layoutParams.leftMargin = 120F.toInt()
            layoutParams.rightMargin = (-200F).toInt()*/
            holder.viewBinding.myFrame.layoutParams = layoutParams

        }else if (holder.layoutPosition in secondColPos){
            val layoutParams =  holder.viewBinding.myFrame.layoutParams as ViewGroup.MarginLayoutParams
            /*layoutParams.leftMargin = 120F.toInt()
            layoutParams.rightMargin = (-200F).toInt()*/
            holder.viewBinding.myFrame.layoutParams = layoutParams

        }
    }

    inner class ViewHolder(var viewBinding: RowSeatItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(seat: Seat) {
            viewBinding.tvSeatNum.text = seat.num.toString()
            if (seat.isSelected && seat.status=="reserved"){
                viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(itemView.context, R.color.gray4), android.graphics.PorterDuff.Mode.SRC_IN)
            }else if (seat.status == "available") {
                viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(itemView.context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            viewBinding.root.setOnClickListener {
                if (!seat.isSelected){
                    seat.status = "selected"
                    viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(itemView.context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN)
                }else if (seat.status =="selected"){
                    viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                iClick.onSeatClicked(seat = seat,bindingAdapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onSeatClicked(seat: Seat, pos:Int)
    }

}