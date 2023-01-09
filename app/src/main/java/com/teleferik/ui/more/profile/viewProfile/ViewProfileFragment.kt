package com.teleferik.ui.more.profile.viewProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentViewProfileBinding
import com.teleferik.models.showProfile.ShowProfileResponse
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.utils.handleApiErrors


class ViewProfileFragment :
    BaseFragment<ProfileViewModel, FragmentViewProfileBinding, ProfileRepo>() {
    private var userData: ShowProfileResponse? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentViewProfileBinding.inflate(layoutInflater)

    override fun handleView() {
        initClicks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       callViewProFileRequest()
    }

    private fun callViewProFileRequest() {
        mViewModel.showProfile()
        observeShowProfile()
    }

    private fun observeShowProfile() {
        mViewModel.showProfileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    updateUI(it.value.data)
                }
                is Resource.Failure -> {
                    loading.cancel()
                    handleApiErrors(it)
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun updateUI(data: ShowProfileResponse?) {
        data?.let {
            userData = it
            binding.tvName.text = it.name
            binding.tvPhone.text = "+${it.country?.phonecode}${it.mobile}"
        }
    }


    private fun initClicks() {
        binding.imgBack.setOnClickListener { findNavController().navigateUp() }
        binding.tvPersonalInfo.setOnClickListener {
            findNavController().navigate(
                ViewProfileFragmentDirections.actionViewProfileFragmentToEditProfileFragment(
                    userData?.name,
                    userData?.email,
                    userData?.mobile,
                    userData?.image
                )
            )
        }
    }

    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun getFragmentRepo(): ProfileRepo {
        return ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
    }
}