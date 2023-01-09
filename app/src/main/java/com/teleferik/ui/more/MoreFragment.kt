package com.teleferik.ui.more

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hmlabs.kheirzaman.utils.localizationUtils.LocaleManager
import com.jakewharton.processphoenix.ProcessPhoenix
import com.teleferik.AppController
import com.teleferik.MainActivity
import com.teleferik.R
import com.teleferik.WebViewActivity
import com.teleferik.base.BaseBindingFragment
import com.teleferik.databinding.FragmentMoreBinding
import com.teleferik.utils.Constants
import com.teleferik.utils.hide
import com.teleferik.utils.putAny

class MoreFragment : BaseBindingFragment<FragmentMoreBinding>() {


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentMoreBinding.inflate(layoutInflater)

    override fun handleView() {
        initClicks()
        handleGuestUser()
        getOtherLang()
    }

    private fun getOtherLang() {
        if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC)
            binding.tvLang.text = "English"
        else binding.tvLang.text = "العربية"
    }

    private fun handleGuestUser() {
        val isUserLoggedIn = AppController.Prefs.getString(Constants.USER_TOKEN, "")
        if (isUserLoggedIn.isNullOrEmpty()) {
            binding.tvProfile.hide()
            binding.tvLogout.hide()
//            binding.tvSupport.hide()
            binding.tvUserName.text = getString(R.string.login)
            binding.tvUserName.paintFlags =
                binding.tvUserName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            binding.tvUserName.setOnClickListener {
                val uri = Uri.parse("android-app://com.teleferik.name/login")
                findNavController().navigate(uri)
            }
        }else{
            val name = AppController.Prefs.getString(Constants.USER_NAME,"")
            binding.tvUserName.text = name
        }
    }

    private fun initClicks() {
        binding.tvTerms.setOnClickListener {
            openTermsScreen()
        }
        binding.tvLogout.setOnClickListener {
            AppController.Prefs.putAny(Constants.USER_TOKEN,"")
            startActivity(Intent(requireActivity(),MainActivity::class.java))
            requireActivity().finish()
        }
        binding.tvAboutApp.setOnClickListener { openAboutAppScreen() }
        binding.tvSupport.setOnClickListener { findNavController().navigate(MoreFragmentDirections.actionNavigationMoreToSupportGraph()) }
        binding.tvProfile.setOnClickListener { findNavController().navigate(MoreFragmentDirections.actionNavigationMoreToProfileGraph()) }
        binding.tvChangeLang.setOnClickListener {
            if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC)
                changeApplicationLanguage(LocaleManager.LANGUAGE_ENGLISH,requireActivity())
            else  changeApplicationLanguage(LocaleManager.LANGUAGE_ARABIC,requireActivity())
        }
    }

    private fun changeApplicationLanguage(lang: String,context: Context) {
        AppController.localeManager!!.setNewLocale(context, lang)
        AppController.Prefs.putAny(Constants.IS_LANG_SELECTED,true)
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        ProcessPhoenix.triggerRebirth(context, intent);
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

    private fun openAboutAppScreen() {
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra(
            Constants.PARAMS.SCREEN_TITLE,
            getString(R.string.about_app)
        )
        intent.putExtra(Constants.PARAMS.SCREEN_URL, Constants.LINKS.FRQ_URL)
        startActivity(intent)
    }


}