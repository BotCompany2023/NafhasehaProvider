package com.sa.nafhasehaprovider.network.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.COUNT_NOTIFICATION
import com.sa.nafhasehaprovider.ui.activity.MainActivity


import org.json.JSONObject

open class FirebaseMessagingService : FirebaseMessagingService() {

    var redirectId = 0
    var notificationId = 0
    var clickAction = 0
    var title = ""
    var nBody = ""
    lateinit var intent: Intent

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        COUNT_NOTIFICATION++
        // Handle incoming FCM messages here
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            // Handle the data payload

            try {

            }catch (e:Exception){}
            val params = remoteMessage.data
            val body = JSONObject(params as Map<*, *>)
            redirectId = remoteMessage.data["type_id"]!!.toInt()
            clickAction = remoteMessage.data["type"]!!.toInt()
            notificationId = body.getString("type_id").toInt()
            title = remoteMessage.data["title"].toString()
            nBody = remoteMessage.data["body"].toString()


            //طلبات
            if (clickAction == 1) {
                Log.i(TAG, "clickActionData : ${clickAction}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", clickAction)
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }
            //خصومات
            else if (clickAction == 2) {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", clickAction)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            //معلومات
            else if (clickAction == 3) {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", clickAction)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else {
                intent = Intent(this, MainActivity::class.java)
            }

        }


        // notification payload
        // Check if message contains a notification payload.
//        if (remoteMessage.notification != null) {
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.title +"\n"+ it.body}")
            // Handle the notification payload
            val click_action = clickAction //get click_action
            var notification = redirectId
            title=it.title.toString()
            nBody=it.body.toString()


            if (click_action == 1) {
                Log.i(TAG, "clickActionNotification : ${clickAction}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", clickAction)
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }
            else if (click_action ==2) {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }
            else if (click_action == 3) {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }
            else {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type", click_action)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }


        }
        // sending notification
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val channelId = "Default"
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_logo_app)
                .setContentTitle(title)
                .setContentText(nBody).setAutoCancel(true)
                .setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        } else {

        }

        manager.notify(0, builder.build())

//        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Handle the new FCM token here
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }

}