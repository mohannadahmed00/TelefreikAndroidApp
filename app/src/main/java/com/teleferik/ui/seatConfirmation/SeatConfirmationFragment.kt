package com.teleferik.ui.seatConfirmation

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSeatConfirmationBinding

import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel


class SeatConfirmationFragment :
    BaseFragment<HomeViewModel, FragmentSeatConfirmationBinding, HomeRepo>(){

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentSeatConfirmationBinding.inflate(layoutInflater)

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        initClicks()
    }
    private fun initClicks() {
        //why?
        //binding.includeTicket.tvRemainingTime.visibility = View.GONE
        binding.includeTopBar.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}