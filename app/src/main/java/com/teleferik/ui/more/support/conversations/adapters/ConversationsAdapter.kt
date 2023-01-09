package com.teleferik.ui.more.support.conversations.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowConversationBinding
import com.teleferik.models.tickets.Ticket
import com.teleferik.utils.getCurrentDate
import com.teleferik.utils.getDate

class ConversationsAdapter(
    var list: MutableList<Ticket>,
    private val iClick: OnItemClickListener
) : RecyclerView.Adapter<ConversationsAdapter.ViewHolder>() {


    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowConversationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(var viewBinding: RowConversationBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(ticket: Ticket) {
            viewBinding.apply {
                tvMessageTitle.text = ticket.title
                tvLastMessage.text = ticket.description
                ticket.createdAt?.let {
                    tvMessageCounter.text = this.root.context.getDate(it, getCurrentDate())
                }
                root.setOnClickListener {
                    iClick.onItemClicked(ticket)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(ticket: Ticket)
    }
}