package com.teleferik.ui.otp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hmlabs.kheirzaman.utils.GenericTextWatcher
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentOtpBinding
import com.teleferik.ui.auth.AuthRepo
import com.teleferik.ui.auth.AuthViewModel
import com.teleferik.utils.*

class OtpFragment : BaseFragment<AuthViewModel, FragmentOtpBinding, AuthRepo>() {
    private val args: OtpFragmentArgs by navArgs()
    private lateinit var currentOTP:String
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOtpBinding {
        return FragmentOtpBinding.inflate(layoutInflater)
    }

    override fun handleView() {
        //currentOTP = args.otp
        //showTopToast(currentOTP)
        /*if (!args.isUserExistBefore){
            sendOTPToNewUser(args.phoneNumber)
        }*/
        handleClicks()
        handleOtp()
        bindPhoneNumber()
        binding.tvTimer.countDownTimer(300000, 1000, binding.tvResend, binding.tvTimer)//edit to 5 mins
    }

    /*private fun sendOTPToNewUser(phoneNumber: String) {
        mViewModel.sendOTP(phoneNumber)

    }*/

    private fun bindPhoneNumber() {
        binding.tvMessageSent.text = getString(R.string.enter_code_hint, "+20${args.phoneNumber}")
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

    private fun handleClicks() {
        binding.tvBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnNext.setOnClickListener {
            binding.tvVerificationError.visibility = View.INVISIBLE
            if (getCodeFromView().isNotEmpty() /*&& getCodeFromView() == currentOTP*/) {
                val otp = HashMap<String,Any>()
                Log.e("ExErrorBody","..${args.phoneNumber}..20..${getCodeFromView()}..")
                /*if (args.isUserExistBefore){
                    mViewModel.oldVerifyOTP(args.phoneNumber,"20",getCodeFromView())
                }else{*/
                mViewModel.verifyOTP(args.phoneNumber,Constants.EGYPT_PHONE_CODE,getCodeFromView())
                //}
                observeVerifyOTP()
            } else {
                binding.tvVerificationError.visibility = View.VISIBLE
            }
        }
        binding.tvResend.setOnClickListener {
            if (binding.tvResend.isEnabled) {
                mViewModel.resendOTP()
                observeResendOTP()
            }
        }
    }

    private fun showCodeSuccessDialog() {
        DialogUtils.showPopupDialog(
            requireActivity(),
            R.drawable.ic_phone_verified_success,
            getString(R.string.phone_number_verified_successfuly)
        ) {
            Log.e("OtpSuccess",args.isUserExistBefore.toString())
            if (args.isUserExistBefore){
                findNavController().setGraph(R.navigation.teleferik_navigation)
            }else{
                findNavController().navigate(OtpFragmentDirections.actionOtpFragmentToSignupFragment().apply {
                    phoneNumber = args.phoneNumber
                })
            }

            /*if (args.isUserExistBefore) {
                findNavController().setGraph(R.navigation.teleferik_navigation)
            } else {
                findNavController().navigate(
                    OtpFragmentDirections.actionOtpFragmentToSignupFragment().apply {
                        phoneNumber = args.phoneNumber
                    }
                )
            }*/
        }
    }

    private fun getCodeFromView(): String {
        return binding.otpET1.text.toString() +
                binding.otpET2.text.toString() +
                binding.otpET3.text.toString() +
                binding.otpET4.text.toString()
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentRepo(): AuthRepo {
        return AuthRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    private fun observeVerifyOTP() {
        Log.e("OtpResponseSuccess", "is exist before-->${args.isUserExistBefore}")
        if (args.isUserExistBefore){
            mViewModel.otpResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        Log.e("OtpResponseSuccess", "${it.value.toString()}..${args.isUserExistBefore}")
                        loading.cancel()
                        showCodeSuccessDialog()
                        mViewModel._loginResponse.value = null
                    }
                    is Resource.Failure -> {
                        Log.e("OtpResponseFailure", it.errorBody.toString())
                        loading.cancel()
                        handleApiErrorsLogin(it)
                        mViewModel._loginResponse.value = null
                    }
                    is Resource.Loading -> {
                        loading.show()
                    }
                }
            }
        }else {
            mViewModel.otpResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        Log.e("OtpResponseSuccess", it.value.data.toString())
                        loading.cancel()
                        showCodeSuccessDialog()
                        mViewModel._loginResponse.value = null
                    }
                    is Resource.Failure -> {
                        Log.e("OtpResponseFailure", it.errorBody.toString())
                        loading.cancel()
                        handleApiErrorsLogin(it)
                        mViewModel._loginResponse.value = null
                    }
                    is Resource.Loading -> {
                        loading.show()
                    }
                }
            }
        }
    }

    private fun observeResendOTP() {
        mViewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    currentOTP = it.value.data?.OTP!!
                    showTopToast(currentOTP)
                    DialogUtils.showPopupDialog(
                        requireActivity(),
                        R.drawable.code_resend_again,
                        getString(R.string.code_resend)
                    ) {
                        binding.tvTimer.showHideView(true)
                        binding.tvTimer.countDownTimer(30000, 1000, binding.tvResend, binding.tvTimer)
                    }
                    mViewModel._loginResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    handleApiErrorsLogin(it)
                    mViewModel._loginResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }
}
