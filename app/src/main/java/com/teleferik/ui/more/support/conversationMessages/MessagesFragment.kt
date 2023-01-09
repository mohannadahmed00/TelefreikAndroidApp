package com.teleferik.ui.more.support.conversationMessages

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentMessagesBinding
import com.teleferik.models.tickets.Reply
import com.teleferik.models.tickets.SingleTicketResponse
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.ui.more.support.conversationMessages.adapter.MessagesAdapter
import com.teleferik.utils.HTTPCODES
import com.teleferik.utils.captureText
import com.teleferik.utils.checkPermissions
import com.teleferik.utils.handleApiErrors
import java.io.File

class MessagesFragment : BaseFragment<ProfileViewModel, FragmentMessagesBinding, ProfileRepo>() {

    private val args: MessagesFragmentArgs by navArgs()
    private var selectedFile: File? = null
    private val mMessagesAdapter: MessagesAdapter by lazy { MessagesAdapter(mutableListOf()) }
    override fun handleView() {
        handleBackPress()
        initMessagesRecyclerView()
        initClicks()
        callSingleTicketRequest(args.ticketId)
    }

    private fun initClicks() {
        binding.imgback.setOnClickListener {
            findNavController().navigate(MessagesFragmentDirections.actionMessagesFragmentPop())
        }
        binding.ivAttachPhoto.setOnClickListener {
            checkPermissions {
                pickImageFromGallery()
            }
        }
        binding.btnSendReply.setOnClickListener {
            if (validateReplyField()) {
                callAddReplyRequest(binding.edReplyMessage.captureText())
            }
        }
    }

    private fun handleBackPress() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(MessagesFragmentDirections.actionMessagesFragmentPop())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun validateReplyField(): Boolean {
        var isValid = true
        if (binding.edReplyMessage.captureText().trim().isEmpty()) {
            binding.edReplyMessage.error = getString(R.string.field_not_empty)
            isValid = false
        } else {
            binding.edReplyMessage.error = null
        }
        return isValid
    }

    private fun callSingleTicketRequest(ticketId: String, showLoading: Boolean = true) {
        mViewModel.getSingleTicket(ticketId)
        observeSingleTicket(showLoading)
    }

    private fun callAddReplyRequest(message: String) {
        mViewModel.addReply(
            ticket_id = args.ticketId,
            user_id = "",
            message = message,
            file = selectedFile
        )
        observeAddReply()
    }

    private fun observeSingleTicket(showLoading: Boolean) {
        mViewModel.singleTicketResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    updateUI(it.value.data!!)

                    mViewModel._singleTicketResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    if (it.errorCode == HTTPCODES.CODEAUTHERROR.code)
                        handleApiErrors(it)
                    mViewModel._singleTicketResponse.value = null
                }
                is Resource.Loading -> {
                    if (showLoading) loading.show() else loading.cancel()
                }
            }
        }
    }

    private fun updateUI(data: SingleTicketResponse) {
        binding.tvTitle.text = data.title
        val ticketData = Reply(
            createdAt = data.createdAt,
            message = data.description
        )
        updateMessagesRecyclerView(data.replies as MutableList<Reply>,ticketData)
        mMessagesAdapter.apply {
            if (mMessageList.isNotEmpty())
                binding.rvMessages.smoothScrollToPosition(mMessageList.lastIndex)
        }
    }

    private fun observeAddReply() {
        mViewModel.addReplyResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    selectedFile = null
                    binding.edReplyMessage.setText("")
                    callSingleTicketRequest(args.ticketId, false)
                    mViewModel._addReplyResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    if (it.errorCode == HTTPCODES.CODEAUTHERROR.code)
                        handleApiErrors(it)
                    mViewModel._addReplyResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        UwMediaPicker
            .with(this)                        // Activity or Fragment
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery) // GalleryMode: ImageGallery/VideoGallery/ImageAndVideoGallery, default is ImageGallery
            .setGridColumnCount(3)                                  // Grid column count, default is 3
            .setMaxSelectableMediaCount(1)                         // Maximum selectable media count, default is null which means infinite
            .setLightStatusBar(true)                                // Is llight status bar enable, default is true
            .enableImageCompression(true)                // Is image compression enable, default is false
            .setCompressionMaxWidth(1280F)                // Compressed image's max width px, default is 1280
            .setCompressionMaxHeight(720F)                // Compressed image's max height px, default is 720
            .setCompressFormat(Bitmap.CompressFormat.JPEG)        // Compressed image's format, default is JPEG
            .setCompressionQuality(85)                // Image compression quality, default is 85
            .launch {
                selectedFile = File(it?.get(0)!!.mediaPath)
                callAddReplyRequest("pic")
            }
    }

    private fun initMessagesRecyclerView() {
        binding.rvMessages.adapter = mMessagesAdapter
    }

    private fun updateMessagesRecyclerView(replies: MutableList<Reply>?, ticketData: Reply) {
            replies?.add(0,ticketData)
            mMessagesAdapter.apply {
                mMessageList.clear()
                mMessageList = replies!!
                notifyDataSetChanged()
            }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentMessagesBinding.inflate(layoutInflater)

    override fun getViewModel() = ProfileViewModel::class.java
    override fun getFragmentRepo() = ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
}