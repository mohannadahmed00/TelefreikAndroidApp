package com.teleferik.utils

import android.animation.Animator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.teleferik.R

object DialogUtils {
    fun showPopupDialog(
        context: Context,
        image: Int = 0,
        title: String = "",
        okListener: () -> Unit
    ): AlertDialog {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.loading_data_dialog,
            null,
            false
        )
        val alertDialog = AlertDialog.Builder(context).apply {
            setView(view)
            setCancelable(false)
        }.create()
        alertDialog.apply {
            val loadingProgress = view.findViewById<LottieAnimationView>(R.id.imgLoadingProgress)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            val imgDialog = view.findViewById<ImageView>(R.id.imgDialog)
            tvTitle.text = title
            imgDialog.setImageResource(image)
            loadingProgress.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    okListener.invoke()
                    dismiss()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {

                }
            })
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }.show()

        return alertDialog
    }

    fun actionPopUp(
        context: Context,
        message: String,
        view: View = LayoutInflater.from(context).inflate(
            R.layout.dialog_alert,
            null,
            false
        ),
        okListener: () -> Unit
    ): AlertDialog {
        val alertDialog = AlertDialog.Builder(context).apply {
            setView(view)
            setCancelable(false)
        }.create()
        alertDialog.apply {
            view.findViewById<TextView>(R.id.tvMessage).text = message
            view.findViewById<Button>(R.id.btnOk).setOnClickListener {
                okListener.invoke()
                dismiss()
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }.show()

        return alertDialog
    }
}