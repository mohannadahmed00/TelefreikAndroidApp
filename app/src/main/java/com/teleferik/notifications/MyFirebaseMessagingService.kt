package com.teleferik.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teleferik.MainActivity
import com.teleferik.R
import com.teleferik.utils.Constants.NOTIFICATION_TYPE
import com.teleferik.utils.Constants.NOTIFICATION_URL
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e("notifications_log", "Message data payload: ${remoteMessage.data}")
            try {
                val json = JSONObject(remoteMessage.data as Map<String, String>)
                val title = json.optString("title")
                val body = json.optString("description")
                val type = json.optInt("type")
                val link = json.optString("link")
                createNotification(title,body,type,link,null)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }


    private fun createNotification(
        notificationTitle: String,
        notificationContent: String,
        type:Int,
        link:String?,
        imageUrl: Uri?
    ) { // Let's create a notification builder object


        val  intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(NOTIFICATION_TYPE,type)
        intent.putExtra(NOTIFICATION_URL,link)
        // set pending intent
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        var sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/raw/notification.mp3");
        val builder = NotificationCompat.Builder(
            this,
            "TELE_ID"
        ).setSound(sound)
        // Create a notificationManager object
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // If android version is greater than 8.0 then create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Create a notification channel
            val notificationChannel = NotificationChannel(
                "TELE_ID",
                "TELE_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )

            var audioAttributes =  AudioAttributes.Builder()
                .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                .setUsage(AudioAttributes. USAGE_ALARM )
                .build()


            notificationChannel.setSound(sound,audioAttributes)
            // Set properties to notification channel
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(300, 200, 300)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // Set the notification parameters to the notification builder object
        builder.setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setSound(sound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }




}