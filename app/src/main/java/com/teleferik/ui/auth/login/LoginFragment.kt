package com.teleferik.ui.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.teleferik.R
import com.teleferik.WebViewActivity
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentLoginBinding
import com.teleferik.ui.auth.AuthRepo
import com.teleferik.ui.auth.AuthViewModel
import com.teleferik.utils.*

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepo>() , FacebookCallback<LoginResult>{

    private lateinit var  mGoogleSignInClient: GoogleSignInClient
    private lateinit var registry: ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun handleView() {
        binding.edtPhone.requestFocus()
        binding.edtPhone.setText("")
        //prepareGoogleSignIn()
        //prepareFacebookSignIn()
        handleClicks()
    }

    private fun handleClicks() {
        binding.btnNext.setOnClickListener {
            binding.edtPhone.error = null
            if (!getPhoneNumber().isValidPhone()) {
                binding.edtPhone.error = getString(R.string.enter_your_phone_number_hint)
            } else {
                Log.e("ResponseLoginNumber",getPhoneNumber())
                mViewModel.login(getPhoneNumber())
                observeLogin()
            }
        }

        binding.tvSkip.setOnClickListener {
            findNavController().setGraph(R.navigation.teleferik_navigation)
        }

        binding.tvTermsAndCondtions.setOnClickListener {
            openTermsScreen()
        }

        binding.btngoogle.setOnClickListener {
            if (getPhoneNumber().isValidPhone()) {
                registry.launch(mGoogleSignInClient.signInIntent)
            }
            else{
                binding.edtPhone.error = getString(R.string.enter_your_phone_number_hint)
            }
        }

        binding.btnfacebook.setOnClickListener {
            if (getPhoneNumber().isValidPhone()) {
                LoginManager.getInstance()
                    .logInWithReadPermissions(requireActivity(), listOf("public_profile", "email"))
            }
            else{
                binding.edtPhone.error = getString(R.string.enter_your_phone_number_hint)
            }
        }
    }

    private fun prepareGoogleSignIn(){
        registry = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleSignInResult(task)
            }
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            /*if(account != null){
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignupFragment().apply {
                        phoneNumber = this@LoginFragment.getPhoneNumber()
                        name = account.displayName!!
                        email = account.email!!
                        suuid = account.idToken!!
                        registrationType = "google"
                    }
                )
                Log.w("Login account", "${account.idToken} - ${account.email} - ${account.displayName}")
            }
            else{
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignupFragment().apply {
                        phoneNumber = this@LoginFragment.getPhoneNumber()
                    }
                )
            }*/
        } catch (e: ApiException) {
//            Log.e("Login Google", "signInResult:failed code= " + e.statusCode)
            showTopToast(getString(R.string.failed_operation))
        }
    }

    private fun prepareFacebookSignIn(){
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn) {
            if (accessToken != null) {
                requestUserInfoFacebook(accessToken)
            }
        }
        else {
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(callbackManager,this)
        }
    }

    private fun handleFacebookSignInResult(result: LoginResult) {
        requestUserInfoFacebook(result.accessToken)
    }

    private fun requestUserInfoFacebook(accessToken: AccessToken){
        Log.e("Login facebook", "get user info")
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { objectData, response ->
            /*if (response != null && objectData != null) {
                Log.e("Login facebook", "done user info")
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignupFragment().apply {
                        phoneNumber = this@LoginFragment.getPhoneNumber()
                        name = objectData.getString("name")
                        email = objectData.getString("email")
                        suuid = accessToken.token
                        registrationType = "facebook"
                    }
                )
            }
            else {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignupFragment().apply {
                    phoneNumber = this@LoginFragment.getPhoneNumber()
                })
            }*/
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,link,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getPhoneNumber(): String {
        var phoneNum = ""
        if (binding.edtPhone.captureText().isNotEmpty()) {
            val length = binding.edtPhone.captureText().length
            if ((binding.edtPhone.captureText()[0] == '0')) {
                if (length > 1) {
                    phoneNum = binding.edtPhone.captureText().substring(
                        1,
                        length
                    )
                }
            }else{
                phoneNum = binding.edtPhone.captureText()
            }
        }
        return phoneNum
    }

    private fun observeLogin() {
        mViewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.e("LoginResponseSuccess",it.value.toString())
                    loading.cancel()
                    /*if (it.value.need_verfication == null){
                        AppController.Prefs.putAny(Constants.USER_NAME, it.value.data?.name!!)
                        AppController.Prefs.putAny(Constants.USER_TOKEN, it.value.data.api_token!!)
                        AppController.Prefs.putAny(Constants.USER_EMAIL,it.value.data.email!!)
                        AppController.Prefs.putAny(Constants.USER_PHONE,this@LoginFragment.getPhoneNumber())
                    }*/
                    /*findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToOtpFragment().apply {
                            //otp = it.value.data?.OTP!!
                            isUserExistBefore = true
                            phoneNumber = this@LoginFragment.getPhoneNumber()
                        })*/
                    mViewModel.sendOTP(this@LoginFragment.getPhoneNumber())//todo check if there is 0 or not
                    observeSendOTP(true)
                    mViewModel._loginResponse.value = null
                }
                is Resource.Failure -> {
                    Log.e("LoginResponseFailure","${it.errorBody.toString()}..${it.errorCode}")
                    loading.cancel()
                    mViewModel.sendOTP(this@LoginFragment.getPhoneNumber())//todo check if there is 0 or not
                    observeSendOTP(false)
                    mViewModel._loginResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    private fun observeSendOTP(isExistUser:Boolean){
        mViewModel.otpResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    Log.e("SendOtpResponseSuccess",it.value.toString())
                    loading.cancel()
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToOtpFragment().apply {
                            isUserExistBefore = isExistUser
                            phoneNumber = this@LoginFragment.getPhoneNumber()
                        })
                    mViewModel._otpResponse.value = null
                }
                is Resource.Failure -> {
                    Log.e("SendOtpResponseFailure","${it.errorBody.toString()}..${it.errorCode}")
                    loading.cancel()
                    mViewModel._otpResponse.value = null
                }
                is Resource.Loading -> {
                    loading.show()
                }
            }
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentRepo(): AuthRepo {
        return AuthRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    private fun openTermsScreen() {
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra(
            Constants.PARAMS.SCREEN_TITLE,
            getString(R.string.terms_and_conditions_)
        )
        intent.putExtra(Constants.PARAMS.SCREEN_URL, Constants.LINKS.TERMS_URL)
        startActivity(intent)
    }

    override fun onCancel() {
    }

    override fun onError(error: FacebookException) {
        showTopToast(getString(R.string.failed_operation))
    }

    override fun onSuccess(result: LoginResult) {
        handleFacebookSignInResult(result)
        Log.e("Login facebook", "done")
    }
}