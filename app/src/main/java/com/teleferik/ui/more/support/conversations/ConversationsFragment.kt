package com.teleferik.ui.more.support.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teleferik.R
import com.teleferik.base.BaseBindingFragment
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentConversationsBinding
import com.teleferik.models.tickets.Ticket
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.ui.more.support.conversations.adapters.ConversationsAdapter
import com.teleferik.utils.DialogUtils
import com.teleferik.utils.HTTPCODES
import com.teleferik.utils.handleApiErrors
import com.teleferik.utils.showHideView
import com.teleferik.utils.showTopToast


class ConversationsFragment : BaseFragment<ProfileViewModel,FragmentConversationsBinding,ProfileRepo>(),
    ConversationsAdapter.OnItemClickListener {

    private val mConversationAdapter: ConversationsAdapter by lazy { ConversationsAdapter(mutableListOf(), this) }

    override fun handleView() {
        initClicks()
        initConversationsRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callTicketsForCustomerResponse()
    }
    private fun callTicketsForCustomerResponse() {
        mViewModel.getTicketsForCustomer()
        observeTicketsForCustomer()
    }

    private fun observeTicketsForCustomer() {
        mViewModel.ticketsForCustomerResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    updateConversationRecyclerView(it.value.data!!.tickets)
                    mViewModel._ticketsForCustomerResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    if (it.errorCode == HTTPCODES.CODEAUTHERROR.code)
                        handleApiErrors(it)
                    mViewModel._ticketsForCustomerResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        })
    }

    private fun updateConversationRecyclerView(tickets: List<Ticket>?) {
        binding.includeNoConversations.linNoMessages.showHideView(tickets.isNullOrEmpty())
        binding.rvConversations.showHideView(!tickets.isNullOrEmpty())
        if (!tickets.isNullOrEmpty()){
            mConversationAdapter.apply {
                list = tickets as MutableList<Ticket>
                notifyDataSetChanged()
            }
        }
    }

    private fun initClicks()
    {
        binding.imgback.setOnClickListener { findNavController().navigateUp() }
        binding.imgNewSupport.setOnClickListener { findNavController().navigate(ConversationsFragmentDirections.actionConversationsFragmentToAddNewSupportTicketFragment()) }
    }

    private fun initConversationsRecyclerView()
    {
        binding.rvConversations.adapter = mConversationAdapter
    }

    override fun onItemClicked(ticket: Ticket) {
        val action = ConversationsFragmentDirections.actionConversationsFragmentToMessagesFragment()
        if (!ticket.id.isNullOrEmpty()) {
            action.ticketId = ticket.id
            findNavController().navigate(action)
        }else{
            showTopToast(getString(R.string.cant_open_ticket))
        }
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentConversationsBinding.inflate(layoutInflater)
    override fun getViewModel() = ProfileViewModel::class.java
    override fun getFragmentRepo() = ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
}