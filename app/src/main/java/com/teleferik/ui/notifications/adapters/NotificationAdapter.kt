package com.teleferik.ui.notifications.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowNotificationsBinding

import com.teleferik.databinding.RowTransportationTypeBinding
import com.teleferik.models.notificationList.NotificationListResponseItem
import com.teleferik.utils.getCurrentDate
import com.teleferik.utils.getDate
import com.teleferik.utils.invisibleView

class NotificationAdapter(
    var list: MutableList<NotificationListResponseItem>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowNotificationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(var viewBinding: RowNotificationsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: NotificationListResponseItem) {
            viewBinding.apply {
                this.tvDate.text = this.root.context.getDate(data.createdAt!!, getCurrentDate())
                this.tvTitle.text = data.title
                this.tvDesc.text = data.description
                root.setOnClickListener {
                    iClick.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: NotificationListResponseItem)
    }
}