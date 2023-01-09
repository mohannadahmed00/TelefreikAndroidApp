package com.teleferik.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentDashboardBinding
import com.teleferik.models.ticketReservation.SingleTicketReservation
import com.teleferik.ui.dashboard.adapters.DashboardAdapter
import com.teleferik.ui.home.HomeFragment
import com.teleferik.utils.showHideView
import com.teleferik.utils.showTopToast

class DashboardFragment : BaseFragment<DashboardViewModel,FragmentDashboardBinding,DashboardRepo>() , DashboardAdapter.OnItemClickListener {
    lateinit var mDashboardAdapter: DashboardAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(layoutInflater)
    }

    object Statuses{
        const val ALL = "All"
        const val WAITING = "Waiting"
        const val UPCOMMING = "Upcomming"
        const val FINISHED = "Finished"
    }

    override fun handleView() {
        ticketReservationsList(Statuses.ALL)
        if (HomeFragment.isNotificationsHasUnread)
            binding.imgNotifications.setImageResource(R.drawable.ic_notification)
        else
            binding.imgNotifications.setImageResource(R.drawable.ic_notifications_black_24dp)

        binding.imgNotifications.setOnClickListener { findNavController().navigate(DashboardFragmentDirections.actionNavigationDashboardToNotificationFragment()) }
        intiClicks()
    }

    override fun getViewModel(): Class<DashboardViewModel> {
        return DashboardViewModel::class.java
    }

    override fun getFragmentRepo(): DashboardRepo {
        return DashboardRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    private fun ticketReservationsList(status:String?) {
        mViewModel.ticketReservationsList(status)
        observeTicketReservationsList()
    }

    private fun observeTicketReservationsList() {
        mViewModel.ticketReservationList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.value.data!!.reservations.isNotEmpty()) {
                        setupTicketReservations(it.value.data.reservations)
                        binding.rvReservations.showHideView(true)
                        binding.emptyImage.showHideView(false)
                        binding.emptyText.showHideView(false)
                    }
                    else{
                        binding.rvReservations.showHideView(false)
                        binding.emptyImage.showHideView(true)
                        binding.emptyText.showHideView(true)
                    }
                }
                is Resource.Failure -> {
                    binding.emptyImage.showHideView(true)
                    binding.emptyText.showHideView(true)
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun setupTicketReservations(data: List<SingleTicketReservation>)
    {
        binding.rvReservations.invalidate()
        mDashboardAdapter = DashboardAdapter(requireContext(),data as MutableList<SingleTicketReservation>, this)
        mDashboardAdapter.notifyItemChanged(0, data.size)
        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.rvReservations.layoutManager = llm
        binding.rvReservations.adapter = mDashboardAdapter
    }

    override fun onItemClicked(item: SingleTicketReservation) {
        findNavController().navigate(
            DashboardFragmentDirections.actionNavigationDashboardToNavigationReservationDetails().apply {
                reservation = item
            }
        )
    }

    private fun intiClicks()
    {
        binding.all.setOnClickListener {
            if(::mDashboardAdapter.isInitialized){
                mDashboardAdapter.clear()
            }
            ticketReservationsList(Statuses.ALL)
        }

        binding.waiting.setOnClickListener {
            if(::mDashboardAdapter.isInitialized){
                mDashboardAdapter.clear()
            }
            ticketReservationsList(Statuses.WAITING)
        }

        binding.upcoming.setOnClickListener {
            if(::mDashboardAdapter.isInitialized){
                mDashboardAdapter.clear()
            }
            ticketReservationsList(Statuses.UPCOMMING)
        }

        binding.finished.setOnClickListener {
            if(::mDashboardAdapter.isInitialized){
                mDashboardAdapter.clear()
            }
            ticketReservationsList(Statuses.FINISHED)
        }
    }
}