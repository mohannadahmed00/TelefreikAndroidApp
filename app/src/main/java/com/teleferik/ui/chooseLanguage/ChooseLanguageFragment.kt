package com.teleferik.ui.chooseLanguage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hmlabs.kheirzaman.utils.localizationUtils.LocaleManager
import com.jakewharton.processphoenix.ProcessPhoenix
import com.teleferik.AppController
import com.teleferik.MainActivity
import com.teleferik.base.BaseBindingFragment
import com.teleferik.databinding.FragmentChooseLanguageBinding
import com.teleferik.utils.Constants
import com.teleferik.utils.putAny

class ChooseLanguageFragment : BaseBindingFragment<FragmentChooseLanguageBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentChooseLanguageBinding {
        return FragmentChooseLanguageBinding.inflate(layoutInflater)
    }

    override fun handleView() {
        binding.btnArabic.setOnClickListener { changeApplicationLanguage(LocaleManager.LANGUAGE_ARABIC,requireActivity()) }
        binding.btnEnglish.setOnClickListener { changeApplicationLanguage(LocaleManager.LANGUAGE_ENGLISH,requireActivity()) }
    }


    private fun changeApplicationLanguage(lang: String,context: Context) {
        AppController.localeManager!!.setNewLocale(context, lang)
        AppController.Prefs.putAny(Constants.IS_LANG_SELECTED,true)
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        ProcessPhoenix.triggerRebirth(context, intent);
    }


}