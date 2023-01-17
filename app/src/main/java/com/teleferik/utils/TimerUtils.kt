package com.teleferik.utils

import android.content.Context
import android.graphics.Paint
import android.os.CountDownTimer
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.teleferik.R
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimerUtils {

    //in 15 seconds
    const val duration = 15L * 1000

    /*fun startTimer(context: Context,minutesView: TextView , secondsView: TextView){
        object : CountDownTimer(duration * 1000,1000){
            override fun onTick(p0: Long) {
                val time = String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(p0),
                    TimeUnit.MILLISECONDS.toMinutes(p0) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(p0)),
                    TimeUnit.MILLISECONDS.toSeconds(p0) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(p0)),
                    Locale.getDefault()
                    )

                val hrMinSecTimer = time.split(":")
                minutesView.text = hrMinSecTimer[1]
                secondsView.text = hrMinSecTimer[2]
            }

            override fun onFinish() {
                minutesView.setTextColor(context.resources.getColor(R.color.red))
                secondsView.setTextColor(context.resources.getColor(R.color.red))
            }

        }.start()
    }*/


    fun TextView.startCountDownTimer(onFinished:() -> Unit) {
        val context = this.context
        val timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                text = "$elapsedMinutes : $elapsedSeconds "
            }

            override fun onFinish() {
                onFinished.invoke()
            }
        }
        timer.start()
    }



}