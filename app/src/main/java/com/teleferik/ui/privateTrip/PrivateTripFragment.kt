package com.teleferik.ui.privateTrip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.tabs.TabLayout
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentHomeBinding
import com.teleferik.databinding.FragmentPrivateTripBinding
import com.teleferik.ui.home.HomeFragment
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.home.adapters.ViewPagerAdapter
import com.teleferik.ui.privateTrip.adapters.TabsAdapter

class PrivateTripFragment :
    BaseFragment<PrivateTripViewModel, FragmentPrivateTripBinding, PrivateRepo>(),
    TabLayout.OnTabSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getViewModel(): Class<PrivateTripViewModel> {
        return PrivateTripViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentPrivateTripBinding = FragmentPrivateTripBinding.inflate(layoutInflater)

    override fun getFragmentRepo(): PrivateRepo {
        return PrivateRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        binding.includeTopBar.topTitle.text = getString(R.string.private_trip)
        binding.includeTopBar.imgBack.setOnClickListener { findNavController().navigateUp() }
        binding.tlPrivateTrips.addTab(binding.tlPrivateTrips.newTab().setText(R.string.private_bus))
        binding.tlPrivateTrips.addTab(binding.tlPrivateTrips.newTab().setText(R.string.private_mini_bus))
        binding.tlPrivateTrips.addTab(binding.tlPrivateTrips.newTab().setText(R.string.private_limousine))
        binding.tlPrivateTrips.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabsAdapter(childFragmentManager, binding.tlPrivateTrips.tabCount)
        /*binding.vpPrivateTrips.adapter = adapter
        binding.vpPrivateTrips.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tlPrivateTrips))*/
        binding.tlPrivateTrips.addOnTabSelectedListener(this)
        DrawableCompat.setTint(
            DrawableCompat.wrap(binding.includeInfo.ivIcon.drawable),
            ContextCompat.getColor(binding.includeInfo.ivIcon.context, R.color.gray_3)
        )
        binding.btnSearch.setOnClickListener { findNavController().navigate(PrivateTripFragmentDirections.actionPrivateFragmentTripToPrivateConfirmationFragment()) }

       // binding.btnSearch.setOnClickListener { findNavController().navigate(PrivateTripFragmentDirections.actionPrivateFragmentTripToPrivateConfirmationFragment()) }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        binding.includeInfo.tvTripType.text = tab!!.text
        when(tab.position){
            0 -> binding.includeInfo.ivIcon.setImageResource(R.drawable.ic_bus)
            1 -> binding.includeInfo.ivIcon.setImageResource(R.drawable.ic_minibus)
            else -> binding.includeInfo.ivIcon.setImageResource(R.drawable.ic_car)
        }
        //Toast.makeText(context,tab!!.text,Toast.LENGTH_LONG).show()

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

}