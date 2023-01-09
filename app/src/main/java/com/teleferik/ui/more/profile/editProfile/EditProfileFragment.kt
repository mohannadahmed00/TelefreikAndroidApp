package com.teleferik.ui.more.profile.editProfile

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentEditProfileBinding
import com.teleferik.models.EditProfileRequest
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.utils.*
import java.io.File


class EditProfileFragment :
    BaseFragment<ProfileViewModel, FragmentEditProfileBinding, ProfileRepo>() {
    private var selectedFile: File? = null
    private val args: EditProfileFragmentArgs by navArgs()
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentEditProfileBinding.inflate(layoutInflater)

    override fun handleView() {
        initClicks()
        updateUi()
    }

    private fun updateUi() {
        binding.edtName.setText(args.name)
        binding.edtMail.setText(args.email)
    }

    private fun isRegisterFormValid(): Boolean {
        binding.edtName.error = null
        binding.edtMail.error = null
        if (binding.edtName.captureText().isEmpty()) {
            binding.edtName.error = getString(R.string.enter_valid_name)
            return false
        }

        if (!binding.edtMail.captureText().isValidEmail()) {
            binding.edtMail.error = getString(R.string.enter_valid_email)
            return false
        }

        return true
    }

    private fun initClicks() {
        binding.btnSaveChanges.setOnClickListener {
            if (isRegisterFormValid())
                callEditProfileRequest()
        }
        binding.image.setOnClickListener {
            checkPermissions {
                pickImageFromGallery()
            }
        }
        binding.imgback.setOnClickListener { findNavController().navigateUp() }
        binding.tvConfirm.setOnClickListener {
            if (!binding.includePhone.edtPhone.captureText().isValidPhone()) {
                binding.includePhone.edtPhone.error = getString(R.string.enter_your_phone_number_hint)
            } else
                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToConfirmPhoneNumberFragment(binding.includePhone.edtPhone.captureText()))
        }
    }

    private fun pickImageFromGallery() {
        UwMediaPicker
            .with(this)                        // Activity or Fragment
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery) // GalleryMode: ImageGallery/VideoGallery/ImageAndVideoGallery, default is ImageGallery
            .setGridColumnCount(3)                                  // Grid column count, default is 3
            .setMaxSelectableMediaCount(1)                         // Maximum selectable media count, default is null which means infinite
            .setLightStatusBar(true)                                // Is llight status bar enable, default is true
            .enableImageCompression(true)                // Is image compression enable, default is false
            .setCompressionMaxWidth(1280F)                // Compressed image's max width px, default is 1280
            .setCompressionMaxHeight(720F)                // Compressed image's max height px, default is 720
            .setCompressFormat(Bitmap.CompressFormat.JPEG)        // Compressed image's format, default is JPEG
            .setCompressionQuality(85)                // Image compression quality, default is 85
            .launch {
                selectedFile = File(it?.get(0)!!.mediaPath)
                callEditProfileRequest()
            }
    }

    private fun callEditProfileRequest() {
        mViewModel.editUserProfile(
            binding.edtMail.captureText(),
            binding.edtName.captureText(),
            selectedFile
        )
        observeEditProfile()
    }

    private fun observeEditProfile() {
        mViewModel.editProfileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    AppController.Prefs.putAny(Constants.USER_NAME, it.value.data?.name!!)
                    findNavController().navigateUp()
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

    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun getFragmentRepo(): ProfileRepo {
        return ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
    }
}