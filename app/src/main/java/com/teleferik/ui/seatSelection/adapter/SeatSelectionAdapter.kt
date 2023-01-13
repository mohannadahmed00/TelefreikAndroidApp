package com.teleferik.ui.seatSelection.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.R
import com.teleferik.databinding.RowSeatItemBinding
import com.teleferik.models.seats.Seat
import com.teleferik.models.seats.Status


class SeatSelectionAdapter(
    var list: MutableList<Seat>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<SeatSelectionAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowSeatItemBinding.inflate(
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
    inner class ViewHolder(var viewBinding: RowSeatItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(seat: Seat) {
            val context = viewBinding.root.context
            viewBinding.tvSeatNum.text = seat.num.toString()
            viewBinding.tvSeatNum.textSize = if (seat.num > 9) 12F else 15F
            viewBinding.imageSeat.setColorFilter(getColor(context = context , status = seat.status))
            viewBinding.imageSeat.setOnClickListener {
                if (seat.status == Status.Available){
                    seat.status = Status.Selected
                    viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(context,R.color.yellow))
                    iClick.onSeatClicked(seat,bindingAdapterPosition)
                }else if (seat.status == Status.Selected){
                    seat.status = Status.Available
                    viewBinding.imageSeat.setColorFilter(ContextCompat.getColor(context,R.color.gray_light))
                    iClick.onSeatClicked(seat,bindingAdapterPosition)
                }

            }
        }
    }

    interface OnItemClickListener {
        fun onSeatClicked(seat: Seat, pos:Int)
    }

    private fun getColor(context: Context,status: Status) : Int{
        return when (status) {
            Status.Available -> ContextCompat.getColor(context,R.color.green)
            Status.Reserved -> ContextCompat.getColor(context,R.color.gray_light)
            else -> ContextCompat.getColor(context,R.color.yellow)
        }

    }
}