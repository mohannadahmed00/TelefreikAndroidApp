package com.teleferik.ui.privateTrip.privateConfirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentPrivateConfirmationBinding
import com.teleferik.databinding.FragmentSeatConfirmationBinding
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.privateTrip.PrivateRepo
import com.teleferik.ui.privateTrip.PrivateTripViewModel
import com.teleferik.utils.TimerUtils
import com.teleferik.utils.TimerUtils.startCountDownTimer
import com.teleferik.utils.countDownTimer
import com.teleferik.utils.showHideView

class PrivateConfirmationFragment : BaseFragment<PrivateTripViewModel, FragmentPrivateConfirmationBinding, PrivateRepo>() {


    override fun getViewModel(): Class<PrivateTripViewModel> {
        return PrivateTripViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentPrivateConfirmationBinding.inflate(layoutInflater)

    override fun getFragmentRepo(): PrivateRepo {
        return PrivateRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        initClicks()
    }

    private fun initClicks() {
        binding.includeTopBar.topTitle.text = getString(R.string.reservation_confirmation)
        binding.includeTopBar.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.includeTicket.tvNote.showHideView(true)
        binding.includeTicket.tvTotalPrice.showHideView(false)
        binding.includeTicket.tvSummary.showHideView(false)
        binding.includeTicket.tvSeatNo.showHideView(false)
        binding.includeTicket.rvSeatNo.showHideView(false)

        binding.tvTimer.startCountDownTimer(onFinished = {
            binding.timerLayout.showHideView(false)
            binding.confirmedLayout.showHideView(true)
            binding.includeTopBar.root.showHideView(false)
            binding.ticketConfirmedTop.showHideView(true)
            binding.btnConfirm.showHideView(false)
            binding.btnToReservations.showHideView(true)
        })
    }


}