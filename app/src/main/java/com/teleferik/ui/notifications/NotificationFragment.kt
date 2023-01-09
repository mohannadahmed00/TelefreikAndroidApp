package com.teleferik.ui.notifications

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teleferik.R
import com.teleferik.WebViewActivity
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentNotificationBinding
import com.teleferik.models.notificationList.NotificationListResponse
import com.teleferik.models.notificationList.NotificationListResponseItem
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.ui.notifications.adapters.NotificationAdapter
import com.teleferik.utils.Constants
import com.teleferik.utils.HTTPCODES
import com.teleferik.utils.handleApiErrors
import com.teleferik.utils.showHideView


class NotificationFragment :
    BaseFragment<ProfileViewModel, FragmentNotificationBinding, ProfileRepo>(),
    NotificationAdapter.OnItemClickListener {

    lateinit var mNotificationAdapter: NotificationAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentNotificationBinding.inflate(layoutInflater)

    override fun handleView() {
        initClicks()
        getNotificationList()
    }

    private fun getNotificationList() {
        mViewModel.notificationsList()
        observeNotificationList()
    }

    private fun observeNotificationList() {
        mViewModel.notificationListResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    initNotificationsRecyclerView(it.value.data)
                }
                is Resource.Failure -> {
                    loading.cancel()
                    if (it.errorCode == HTTPCODES.CODEURLNOTFOUND.code)
                        binding.tvNoNotifications.showHideView(true)
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun initClicks() {
        binding.imgback.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initNotificationsRecyclerView(data: NotificationListResponse?) {
        mNotificationAdapter = NotificationAdapter(data!!, this)
        binding.rvNotifications.adapter = mNotificationAdapter
    }


    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun getFragmentRepo(): ProfileRepo {
        return ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun onItemClicked(item: NotificationListResponseItem) {
        mViewModel.updateNotification(item.id.toString())
        if (!item.link.isNullOrEmpty())
            openWebView(item.link)
        else {
            if (item.pnotificationTypeId == Constants.NOTIFICATIONS_TYPE.SUPPORT_TICKET)
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToSupportGraph())
        }
    }


    private fun openWebView(url:String)
    {
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra(
            Constants.PARAMS.SCREEN_TITLE,
            ""
        )
        intent.putExtra(Constants.PARAMS.SCREEN_URL, url)
        startActivity(intent)
    }

}