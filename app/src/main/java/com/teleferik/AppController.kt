package com.teleferik

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.hmlabs.kheirzaman.utils.localizationUtils.LocaleManager

class AppController : Application() {

    companion object {
        lateinit var Prefs: SharedPreferences
        lateinit var appInstance: AppController
            private set


        @Synchronized
        fun getInstance(): Context? {
            return appInstance
        }

        var localeManager: LocaleManager? = null
    }



    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        // apart from your code
        Prefs = PreferenceManager.getDefaultSharedPreferences(this)
        appInstance = this
        FirebaseApp.initializeApp(this)
    }



    override fun attachBaseContext(base: Context?) {
        localeManager =
            LocaleManager(base)
        super.attachBaseContext(localeManager!!.setLocale(base!!))
        //Log.d(AppController.TAG, "attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager!!.setLocale(this)
        //Log.d(AppController.TAG, "onConfigurationChanged: " + newConfig.locale.language)
    }


}