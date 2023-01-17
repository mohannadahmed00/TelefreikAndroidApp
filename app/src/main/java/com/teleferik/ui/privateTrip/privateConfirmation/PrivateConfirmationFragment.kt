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
        binding.includeTopBar.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}