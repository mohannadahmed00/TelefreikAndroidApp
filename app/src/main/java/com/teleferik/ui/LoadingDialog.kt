package com.teleferik.ui

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.teleferik.R

class LoadingDialog(context:Context) :Dialog(context)  {

    init {
        requestWindowFeature(1)
        setCancelable(false)
        setContentView(R.layout.loading_dialog)
        window!!.setBackgroundDrawable(ColorDrawable(0))
    }
}