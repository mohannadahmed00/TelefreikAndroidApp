package com.teleferik.ui.more.profile.confirmPhoneChangedPhoneNumber

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.messaging.FirebaseMessaging
import com.hmlabs.kheirzaman.utils.GenericTextWatcher
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentOtpBinding
import com.teleferik.models.EditPhoneRequest
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.ui.otp.OtpFragmentArgs
import com.teleferik.ui.otp.OtpFragmentDirections
import com.teleferik.utils.*


class ConfirmPhoneNumberFragment : BaseFragment<ProfileViewModel,FragmentOtpBinding,ProfileRepo>() {
    private  var fcmToken: String = ""
    private val args: OtpFragmentArgs by navArgs()
    private lateinit var currentOTP:String

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentOtpBinding.inflate(layoutInflater)

    override fun handleView() {
        currentOTP = args.otp
        getFcmToken()
        initClicks()
        handleOtp()
        bindPhoneNumber()
        binding.tvTimer.countDownTimer(30000,1000,binding.tvResend,binding.tvTimer)
    }

    private fun callEditPhoneNumber() {
        mViewModel.editUserMobile(EditPhoneRequest(args.phoneNumber,fcmToken))
        mViewModel.verifyOTP(currentOTP)
        observeVerifyOTP()
    }

//    private fun observeEditPhone() {
//        mViewModel.editPhoneResponse.observe(viewLifecycleOwner) {
//            when (it) {
//                is Resource.Success -> {
//                    loading.cancel()
//                    DialogUtils.showPopupDialog(
//                        requireActivity(),
//                        R.drawable.ic_phone_verified_success,
//                        getString(R.string.phone_number_verified_successfuly)
//                    ) {
//                        findNavController().navigateUp()
//                    }
//                    AppController.Prefs.putAny(Constants.USER_TOKEN, it.value.data?.token!!)
//                    mViewModel._editPhoneResponse.value = null
//                }
//                is Resource.Failure -> {
//                    loading.cancel()
//
//                    mViewModel._editPhoneResponse.value = null
//                }
//                is Resource.Loading -> {
//                    loading.show()
//                }
//            }
//        }
//    }

    private fun initClicks()
    {
        binding.tvBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnNext.setOnClickListener {  callEditPhoneNumber() }
        binding.tvResend.setOnClickListener {
            if (binding.tvResend.isEnabled) {
                mViewModel.resendOTP()
                observeResendOTP()
            }
        }
    }
    private fun bindPhoneNumber() {
        binding.tvMessageSent.text = getString(R.string.enter_code_hint,"+20${args.phoneNumber}")
    }

    private fun handleOtp() {
        binding.otpET1.addTextChangedListener(GenericTextWatcher(binding.otpET1, binding.otpET2))
        binding.otpET2.addTextChangedListener(GenericTextWatcher(binding.otpET2, binding.otpET3))
        binding.otpET3.addTextChangedListener(GenericTextWatcher(binding.otpET3, binding.otpET4))
        binding.otpET4.addTextChangedListener(GenericTextWatcher(binding.otpET4, null))
        binding.otpET1.setOnKeyListener(GenericKeyEvent(binding.otpET1, null))
        binding.otpET2.setOnKeyListener(GenericKeyEvent(binding.otpET2, binding.otpET1))
        binding.otpET3.setOnKeyListener(GenericKeyEvent(binding.otpET3, binding.otpET2))
        binding.otpET4.setOnKeyListener(GenericKeyEvent(binding.otpET4, binding.otpET3))
    }

    override fun getViewModel(): Class<ProfileViewModel> {
       return ProfileViewModel::class.java
    }

    override fun getFragmentRepo(): ProfileRepo {
       return ProfileRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenData ->
            if (tokenData.isComplete) {
                kotlin.runCatching {
                    tokenData.result.toString()
                }.onSuccess {
                    fcmToken = it
                }
            }
        }
    }

    private fun observeVerifyOTP() {
        mViewModel.editPhoneResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    showCodeSuccessDialog()
                    AppController.Prefs.putAny(Constants.USER_TOKEN, it.value.data?.token!!)
                    mViewModel._editPhoneResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    handleApiErrorsLogin(it)
                    mViewModel._editPhoneResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun observeResendOTP() {
        mViewModel.editPhoneResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    currentOTP = it.value.data?.OTP!!
                    DialogUtils.showPopupDialog(
                        requireActivity(),
                        R.drawable.code_resend_again,
                        getString(R.string.code_resend)
                    ) {
                        binding.tvTimer.showHideView(true)
                        binding.tvTimer.countDownTimer(30000, 1000, binding.tvResend, binding.tvTimer)
                    }
                    mViewModel._editPhoneResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    handleApiErrorsLogin(it)
                    mViewModel._editPhoneResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun showCodeSuccessDialog() {
        DialogUtils.showPopupDialog(
            requireActivity(),
            R.drawable.ic_phone_verified_success,
            getString(R.string.phone_number_verified_successfuly)
        ) {
            findNavController().navigateUp()
        }
    }
}