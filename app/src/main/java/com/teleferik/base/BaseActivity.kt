package com.teleferik.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.teleferik.AppController.Companion.localeManager
import com.teleferik.utils.localizationUtils.Utility

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    companion object {
        var lang: String? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(localeManager!!.setLocale(base!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        super.onCreate(savedInstanceState)
        // AndroidThreeTen.init(this)
        Utility.resetActivityTitle(this)
    }


}
