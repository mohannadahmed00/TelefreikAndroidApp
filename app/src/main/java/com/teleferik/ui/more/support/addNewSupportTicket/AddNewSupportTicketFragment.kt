package com.teleferik.ui.more.support.addNewSupportTicket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseBindingFragment
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentAddNewSupportTicketBinding
import com.teleferik.models.tickets.CreateTicketBodyRequest
import com.teleferik.ui.auth.login.LoginFragmentDirections
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.utils.*


class AddNewSupportTicketFragment : BaseFragment<ProfileViewModel,FragmentAddNewSupportTicketBinding,ProfileRepo>() {

    override fun handleView() {
        initClicks()
    }

    private fun initClicks() {
        binding.imgback.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSend.setOnClickListener {
            if (validateFields()){
                mViewModel.createTicket(CreateTicketBodyRequest(
                    title = binding.edMessageTitle.captureText(),
                    description = binding.edMessageContent.captureText()
                ))
                observeCreateTicket()
            }
        }
    }

    private fun observeCreateTicket() {
        mViewModel.createTicketResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    navigateToMessages(it.value.data!!.id)
                    mViewModel._createTicketResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    if (it.errorCode == HTTPCODES.CODEAUTHERROR.code)
                        handleApiErrors(it)
                    mViewModel._createTicketResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun navigateToMessages(ticketID: String?) {
        val action = AddNewSupportTicketFragmentDirections.actionAddNewSupportTicketFragmentToMessagesFragment()
        if (ticketID != null) {
            action.ticketId = ticketID
            findNavController().navigate(action)
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        if (binding.edMessageTitle.captureText().trim().isEmpty()){
            binding.edMessageTitle.error = getString(R.string.field_not_empty)
            isValid = false
        }else{ binding.edMessageTitle.error = null }

        if (binding.edMessageContent.captureText().trim().isEmpty()){
            binding.edMessageContent.error = getString(R.string.field_not_empty)
            isValid = false
        }else{ binding.edMessageContent.error = null }
        return isValid
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentAddNewSupportTicketBinding.inflate(layoutInflater)
    override fun getViewModel() = ProfileViewModel::class.java
    override fun getFragmentRepo() = ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
}