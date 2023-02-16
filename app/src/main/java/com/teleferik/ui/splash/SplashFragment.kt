package com.teleferik.ui.splash

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseBindingFragment
import com.teleferik.databinding.FragmentSplashBinding
import com.teleferik.utils.Constants




class SplashFragment : BaseBindingFragment<FragmentSplashBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.statusBarColor = Color.parseColor("#1D4179")
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireActivity(), R.color.base_app_color)
    }

    override fun handleView() {
        YoYo.with(Techniques.FadeIn).duration(3000).onEnd {
            val isLangSelected = AppController.Prefs.getBoolean(Constants.IS_LANG_SELECTED,false)
            val isFirstOpen = AppController.Prefs.getBoolean(Constants.IS_FIRST_OPEN,true)
            val isUserLoggedIn = AppController.Prefs.getString(Constants.USER_TOKEN,"")
            if (isLangSelected)
            {
                if (isUserLoggedIn.isNullOrEmpty()) {
                    if (isFirstOpen){
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragment2ToOnBoardingGraph())
                        findNavController().setGraph(R.navigation.on_boarding_graph)
                    }else{
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragment2ToAuthGraph())
                        findNavController().setGraph(R.navigation.auth_graph)
                    }

                }else{
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragment2ToMobileNavigation())
                    findNavController().setGraph(R.navigation.teleferik_navigation)
                }
            }else{
                findNavController().navigate(SplashFragmentDirections.actionSplashFragment2ToLanguageGraph4())
                findNavController().setGraph(R.navigation.language_graph)
            }

        }.playOn(binding.imgAppLogo)
    }


}