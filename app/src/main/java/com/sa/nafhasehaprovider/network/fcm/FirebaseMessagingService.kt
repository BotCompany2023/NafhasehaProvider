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
import com.sa.nafhasehaprovider.ui.activity.notification.NotificationActivity


import org.json.JSONException
import org.json.JSONObject

open class FirebaseMessagingService : FirebaseMessagingService() {

    var redirectId  = ""
    var notificationId = 0
    var clickAction  = ""
    var orderStep  = ""
    var title = ""
    var nBody = ""
    lateinit var intent: Intent

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        COUNT_NOTIFICATION++

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.i(TAG, "data payload: ${remoteMessage.data}")

            try {
                val params = remoteMessage.data
                val body = JSONObject(params as Map<*, *>)
                redirectId = remoteMessage.data["type_id"]!!.toString()
                clickAction = remoteMessage.data["type"].toString()
                orderStep = remoteMessage.data["order_step"].toString()
                notificationId = body.getString("type_id").toInt()
                title = body.getString("title").toString()
                nBody = body.getString("body").toString()


                //عرض صفحه تفاصيل الطلب
                if (clickAction == "1") {
                    Log.i(TAG, "clickActionData : ${clickAction}")
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type_id", redirectId)
                    intent.putExtra("type", clickAction)
                    intent.putExtra("title", title)
                    intent.putExtra("body", nBody)
                    intent.putExtra("order_step", orderStep)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                }
                //عرض صفحه الكوبونات
                else if (clickAction == "2") {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type_id", redirectId)
                    intent.putExtra("type", clickAction)
                    intent.putExtra("order_step", orderStep)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                //عرض صفحه الاشعارات
                else if (clickAction == "3") {
                    intent = Intent(this, NotificationActivity::class.java)
                    intent.putExtra("type_id", redirectId)
                    intent.putExtra("type", clickAction)
                    intent.putExtra("order_step", orderStep)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                //صفحه عروض الطلب
                else if (clickAction == "4") {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type_id", redirectId)
                    intent.putExtra("type", clickAction)
                    intent.putExtra("order_step", orderStep)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else {
                    intent = Intent(this, MainActivity::class.java)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }


        // notification payload
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {

            val click_action = clickAction //get click_action
            val _orderStep = orderStep //get click_action
            var _redirectId = redirectId
            title=remoteMessage.notification!!.title.toString()
            nBody=remoteMessage.notification!!.body.toString()




            if (click_action == "1") {
                Log.i(TAG, "clickActionNotificationOrderStep : ${_redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", _redirectId)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", click_action)
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.putExtra("order_step", _orderStep)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "2") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", _redirectId)
                intent.putExtra("type", click_action)
                intent.putExtra("order_step", _orderStep)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "3") {
                Log.i(TAG, "click action = 4 , redirectid ${redirectId}")
                intent = Intent(this, NotificationActivity::class.java)
                intent.putExtra("type_id", _redirectId)
                intent.putExtra("type", click_action)
                intent.putExtra("order_step", _orderStep)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "4") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", _redirectId)
                intent.putExtra("type", click_action)
                intent.putExtra("order_step", _orderStep)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }  else {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type", click_action)
                intent.putExtra("type_id", _redirectId)
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.putExtra("order_step", _orderStep)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            // sending notification
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
            val channelId = "Default"
            val notificationBuilder : NotificationCompat.Builder =
                NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_logo_app)
                    .setContentTitle(title)
                    .setContentText(nBody)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
            } else {

            }
            // عرض الإشعار
            manager.notify(0, notificationBuilder.build())
        }

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