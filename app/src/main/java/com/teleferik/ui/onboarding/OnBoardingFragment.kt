package com.teleferik.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseBindingFragment
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentOnBoardingBinding
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.utils.Constants
import com.teleferik.utils.showHideView
import com.teleferik.utils.invisibleView
import com.teleferik.utils.putAny


class  OnBoardingFragment : BaseFragment<HomeViewModel,FragmentOnBoardingBinding,HomeRepo>() {
    var index =1
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentOnBoardingBinding {
        return FragmentOnBoardingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun handleView() {
        AppController.Prefs.putAny(Constants.IS_FIRST_OPEN,false)
        binding.btnNext.setOnClickListener {
            when (index) {
                0 -> {
                    binding.ivOnboardingDots.setImageResource(R.drawable.on_boarding_dots_1)
                    index++
                }
                1 -> {
                    binding.ivOnboardingDots.setImageResource(R.drawable.on_boarding_dots_2)
                    index++
                    // play ainmation to hide current image and show next one
                    YoYo.with(Techniques.FadeOut).onEnd {
                        binding.imgOnBoarding.setImageResource(R.drawable.on_boarding_2)
                        YoYo.with(Techniques.FadeIn).duration(200).playOn(binding.imgOnBoarding)
                    }.duration(200).playOn(binding.imgOnBoarding)
                    binding.btnSkip.text = getString(R.string.back)
                }
                2 -> {
                    binding.ivOnboardingDots.setImageResource(R.drawable.on_boarding_dots_3)
                    index++
                    YoYo.with(Techniques.FadeOut).onEnd {
                        binding.imgOnBoarding.setImageResource(R.drawable.on_boarding_3)
                        YoYo.with(Techniques.FadeIn).duration(200).playOn(binding.imgOnBoarding)
                    }.duration(200).playOn(binding.imgOnBoarding)
                    YoYo.with(Techniques.FadeInUp).onStart {
                        binding.btnEnter.showHideView(true)
                        binding.btnNext.invisibleView(false)
                        binding.btnSkip.invisibleView(false)
                    }.playOn(binding.btnEnter)
                    binding.btnSkip.text = getString(R.string.back)

                }
            }

            binding.btnEnter.setOnClickListener {
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToAuthGraph())
                findNavController().setGraph(R.navigation.auth_graph)
            }
        }
        binding.btnSkip.setOnClickListener {
            if (binding.btnSkip.text == getString(R.string.back)){
                when (index) {
                    2 -> {
                        index--
                        binding.ivOnboardingDots.setImageResource(R.drawable.on_boarding_dots_1)
                        YoYo.with(Techniques.FadeOut).onEnd {
                            binding.imgOnBoarding.setImageResource(R.drawable.on_boarding_1)
                            YoYo.with(Techniques.FadeIn).duration(200).playOn(binding.imgOnBoarding)
                        }.duration(200).playOn(binding.imgOnBoarding)
                        binding.btnSkip.text = getString(R.string.skip)
                    }
                }
            }
            else {
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment())
                findNavController().setGraph(R.navigation.teleferik_navigation)
            }

        }
    }

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }


}