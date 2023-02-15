package com.teleferik.ui.auth.singup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.messaging.FirebaseMessaging
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSignupBinding
import com.teleferik.models.RegisterRequest
import com.teleferik.ui.auth.AuthRepo
import com.teleferik.ui.auth.AuthViewModel
import com.teleferik.utils.*


class SignupFragment : BaseFragment<AuthViewModel, FragmentSignupBinding, AuthRepo>() {
    private val args: SignupFragmentArgs by navArgs()
    private var fcmToken = ""

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(layoutInflater)
    }

    override fun handleView() {
        if (args.email.isNotEmpty())//from social
            //binding.edtMail.setText(args.email)
        if (args.name.isNotEmpty())//from social
            //binding.edtName.setText(args.name)
        initClicks()
        getFcmToken()
    }

    private fun initClicks() {
        binding.tvBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnSignup.setOnClickListener {
            if (isRegisterFormValid())
            {
                if (args.registrationType == "default")
                {
                    callRegisterRequest()
                }
                else{
                    callSocialRegister(
                        RegisterRequest(
                            args.phoneNumber,
                            binding.edtName.captureText(),
                            binding.edtMail.captureText(),
                            fcmToken,
                            args.registrationType,
                            args.suuid
                        )
                    )
                }
            }
        }
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

    private fun callRegisterRequest() {
        Log.e("RegisterRequest","${args.phoneNumber}..${binding.edtMail.captureText()}..${binding.edtName.captureText()}..${fcmToken}")
        mViewModel.register(
            RegisterRequest(
                binding.edtMail.captureText(),
                args.phoneNumber,
                binding.edtName.captureText(),
                fcmToken
            )
        )
        observeRegister()
    }

    private fun observeRegister() {
        mViewModel.loginResponse.observe(viewLifecycleOwner) {
            Log.e("RegisterRequest","${args.phoneNumber}-->${binding.edtMail.captureText()}-->${binding.edtName.captureText()}-->${fcmToken}")
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    Log.e("RegisterResponseSuccess",it.value.data.toString())
                    //AppController.Prefs.putAny(Constants.USER_TOKEN, it.value.data?.token!!)
                    //AppController.Prefs.putAny(Constants.USER_NAME, it.value.data.name!!)
                    DialogUtils.showPopupDialog(
                        requireActivity(),
                        R.drawable.user_singup_success,
                        getString(R.string.account_created)
                    ) {
                        findNavController().setGraph(R.navigation.teleferik_navigation)
                    }
                    mViewModel._loginResponse.value = null

                    /*findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToOtpFragment().apply {
                        //otp = it.value.data?.OTP!!
                        isUserExistBefore = false
                        phoneNumber = args.phoneNumber
                    })*/

                }
                is Resource.Failure -> {
                    loading.cancel()
                    Log.e("RegisterResponseFailure","${it.errorCode}-->${it.errorBody}")

                    handleApiErrors(failure = it, edtToShowValidation = binding.edtMail)
                    mViewModel._loginResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    override fun getViewModel(): Class<AuthViewModel> {
        return AuthViewModel::class.java
    }

    override fun getFragmentRepo(): AuthRepo {
        return AuthRepo(remoteDataSource.buildApi(ApisService::class.java))
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

    private fun callSocialRegister(registerRequest: RegisterRequest) {
        mViewModel.socialRegister(registerRequest)
        observeSocialRegister()
    }

    private fun observeSocialRegister() {
        mViewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    loading.cancel()
                    AppController.Prefs.putAny(Constants.USER_TOKEN, it.value.data?.token!!)
                    DialogUtils.showPopupDialog(
                        requireActivity(),
                        R.drawable.user_singup_success,
                        getString(R.string.account_created)
                    ) {
                        findNavController().setGraph(R.navigation.teleferik_navigation)
                    }
                    AppController.Prefs.putAny(Constants.USER_NAME, it.value.data.name!!)
                    mViewModel._loginResponse.value = null
                }
                is Resource.Failure -> {
                    loading.cancel()
                    handleApiErrors(failure = it, edtToShowValidation = binding.edtMail)
                    mViewModel._loginResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }
}