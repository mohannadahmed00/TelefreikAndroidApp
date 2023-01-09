package com.teleferik.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentReservationDetailsBinding
import com.teleferik.models.ticketReservation.SingleTicketReservation
import com.teleferik.ui.otp.OtpFragmentDirections

class ReservationDetailsFragment : BaseFragment<DashboardViewModel,FragmentReservationDetailsBinding,DashboardRepo>() {
    private val args: ReservationDetailsFragmentArgs by navArgs()
    private lateinit var reservation: SingleTicketReservation
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentReservationDetailsBinding {
        return FragmentReservationDetailsBinding.inflate(layoutInflater)
    }

    override fun handleView() {
        reservation = args.reservation!!
        initClicks()
        setUpView()
    }

    override fun getViewModel(): Class<DashboardViewModel> {
        return DashboardViewModel::class.java
    }

    override fun getFragmentRepo(): DashboardRepo {
        return DashboardRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    private fun initClicks() {
        binding.imgback.setOnClickListener { findNavController().navigateUp() }
        binding.report.setOnClickListener { findNavController().navigate(ReservationDetailsFragmentDirections.actionNavigationReservationDetailsToSupportGraph()) }
    }

    private fun setUpView(){
        binding.apply {
            tvCarriers.text = resources.getString(R.string.plane)
            tvDestinationStation.text = reservation.arrival
            tvOriginStation.text = reservation.departure
            tvDates.text = reservation.date
        }

        binding.incInboundTrip.apply {
            tvInboundCarriers.text = resources.getString(R.string.plane)
            tvInboundDestinationStation.text = reservation.arrival
            tvInboundOriginStation.text = reservation.departure
            tvInboundDates.text = reservation.date
        }

        binding.tvPrice.text = reservation.price.toString()
    }
}