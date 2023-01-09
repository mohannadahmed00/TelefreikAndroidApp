package com.teleferik.ui.more.support.conversationMessages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teleferik.databinding.RowMessageReceivedBinding
import com.teleferik.databinding.RowMessageSentBinding
import com.teleferik.models.tickets.Reply
import com.teleferik.utils.Constants.datesAreSimilar
import com.teleferik.utils.Constants.getDateHeader
import com.teleferik.utils.getCurrentDate
import com.teleferik.utils.getDate
import com.teleferik.utils.loadImage
import com.teleferik.utils.showHideView

class MessagesAdapter(
    var mMessageList: MutableList<Reply>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    // Determines the appropriate ViewType according to the sender of the message.
    override fun getItemViewType(position: Int): Int {
        val message = mMessageList[position]
        return when (message.user) {
            null -> { // If the current user is the sender of the message
                VIEW_TYPE_MESSAGE_SENT
            }
            else -> { // If some other user sent the message
                VIEW_TYPE_MESSAGE_RECEIVED
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Inflates the appropriate layout according to the ViewType.
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            return SentMessageHolder(
                RowMessageSentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            return ReceivedMessageHolder(
                RowMessageReceivedBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
        return null!!
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> { // want to check current data and compare it with
                (holder as SentMessageHolder).bindSendMessage(mMessageList[position],position)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                (holder as ReceivedMessageHolder).bindSendMessage(mMessageList[position],position)
            }
        }
    }

    private inner class SentMessageHolder(private val rowSentBinding: RowMessageSentBinding) :
        RecyclerView.ViewHolder(rowSentBinding.root) {
        fun bindSendMessage(messageItem: Reply, position: Int) {
            rowSentBinding.apply {
                tvReceivedMessage.text = messageItem.message
                if (position == 0 ){
                    messageItem.createdAt?.let {
                        tvGroupedDate.text = getDateHeader(root.context,it)
                    }
                    tvGroupedDate.showHideView(true)
                }else{
                    if (datesAreSimilar(mMessageList[position-1].createdAt!!,messageItem.createdAt!!)){
                        tvGroupedDate.showHideView(false)
                    }else{
                        messageItem.createdAt.let {
                            tvGroupedDate.text = getDateHeader(root.context,it)
                        }
                    }
                }
                messageItem.createdAt?.let {
                    tvReceivedMessageDate.text = this.root.context.getDate(it, getCurrentDate())
                }
                if (messageItem.attachment.isNullOrEmpty()) {
                    ivReceivedImage.showHideView(false)
                    tvReceivedMessage.showHideView(true)
                }else{
                    ivReceivedImage.showHideView(true)
                    tvReceivedMessage.showHideView(false)
                    ivReceivedImage.loadImage(messageItem.attachment)
                }
            }
        }
    }

    private inner class ReceivedMessageHolder(private val rowMessageReceivedBinding: RowMessageReceivedBinding) :
        RecyclerView.ViewHolder(rowMessageReceivedBinding.root) {
        fun bindSendMessage(message: Reply, position: Int) {
            rowMessageReceivedBinding.apply {
                tvReceivedMessage.text = message.message
                if (position == 0 ){
                    message.createdAt?.let {
                        tvGroupedDate.text = getDateHeader(root.context,it)
                    }
                    tvGroupedDate.showHideView(true)
                }else{
                    if (datesAreSimilar(mMessageList[position-1].createdAt!!,message.createdAt!!)){
                        tvGroupedDate.showHideView(false)
                    }else{
                        message.createdAt.let {
                            tvGroupedDate.text = getDateHeader(root.context,it)
                        }
                    }
                }
                message.createdAt?.let {
                    tvReceivedMessageDate.text = this.root.context.getDate(it, getCurrentDate())
                }
                if (message.attachment.isNullOrEmpty()) {
                    ivReceivedImage.showHideView(false)
                    tvReceivedMessage.showHideView(true)
                }else{
                    ivReceivedImage.showHideView(true)
                    tvReceivedMessage.showHideView(false)
                    ivReceivedImage.loadImage(message.attachment)
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }


}