package com.sa.nafhasehaprovider.network.fcm

import android.app.Notification
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

import org.json.JSONException
import org.json.JSONObject

open class FirebaseMessagingService : FirebaseMessagingService() {

    var redirectId = 0
    var notificationId = 0
    var badge = 0
    var title = ""
    var nBody = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        COUNT_NOTIFICATION++
        
        Log.i("TAGwsltttt", "wsltttt ")
        // Check if message contains a data payload.
//        Log.i(TAG, "Message data payload: " + remoteMessage.data)
        if (remoteMessage.data.isNotEmpty()) {
            Log.i("TAGwsltttt", "wsltttt  data ${remoteMessage.data}")


            try {
                Log.i(TAG, "wsltttt  data ")
                val params = remoteMessage.data
                val body = JSONObject(params as Map<*, *>)
                redirectId = remoteMessage.data["type_id"]!!.toInt()
                notificationId = body.getString("notification_id").toInt()
//                badge = body.getString("badge").toInt()
//                title = body.getString("title").toString()
//                nBody = body.getString("body").toString()

                var intent: Intent
                //معلومات
                if (body.getString("type") == "Info") {
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type_id", body.getString("type_id"))
                    intent.putExtra("type", body.getString("type"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                }
                //تقرير اليوم
                else if (body.getString("type") == "DailyReport") {
                    Log.i(
                        TAG,
                        " data  click action =2 , redirectid string ${body.getString("type_id")} redirectid int ${
                            body.getInt("type_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type", body.getString("type"))
                    intent.putExtra("type_id", body.getString("type_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                //تفاصيل الدايت
                else if (body.getString("type") == "Diet") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("type_id")} redirectid int ${
                            body.getInt("type_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type", body.getString("type"))
                    intent.putExtra("type_id", body.getString("type_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else {
                    intent = Intent(this, MainActivity::class.java)
                }

                // sending notification
                val pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
                val channelId = "Default"
                val builder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_logo_app)
                        .setContentTitle(remoteMessage.notification!!.title)
                        .setContentText(remoteMessage.notification!!.body).setAutoCancel(true)
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

//                manager.notify(body.getString("type").toInt(),builder.build())


            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }


        // notification payload
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {

            Log.i(
                TAG,
                " notification , redirectid ${remoteMessage.data["type_id"]!!} click action ${remoteMessage.notification!!.clickAction}"
            )
            val type = remoteMessage.notification!!.clickAction //get type
            var redirectId = remoteMessage.data["type_id"]!!
            var notification = remoteMessage.data["id"]!!

            var intent: Intent
            Log.d("hghshghgsahgsa","hfjhfdjhf")

            if (type == "Info") {
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (type == "DailyReport") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", type)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (type == "Diet") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("type_id", redirectId)
                intent.putExtra("type", type)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }  else {
                intent = Intent(this, Notification::class.java)
                intent.putExtra("notificationId", notification.toInt())
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            // sending notification
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
            val channelId = "Default"
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_logo_app)
                    .setContentTitle(remoteMessage.notification!!.title)
                    .setContentText(remoteMessage.notification!!.body).setAutoCancel(true)
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

//            manager.notify(type.toInt(),builder.build())


            /*  if (type == "30" ||type == "31"||type == "32" ){
                  manager.notify(redirectId.toInt(), builder.build())

              }else {
                  manager.notify(notificationId, builder.build())
              }
  */
//            manager.notify(notificationId, builder.build())

        }

    }

    /* private fun sendNotification(messageBody: String,title: String,notification_id: String,
                                  type: String,type_id: String,id: String,name: String
                                  ,image: String,myToken: String) {
         val intent = Intent(this, HomeActivity::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         intent.putExtra("body",messageBody)
         intent.putExtra("title",title)
         intent.putExtra("type",type)
         intent.putExtra("notification_id",notification_id)
         intent.putExtra("type_id",type_id)
         intent.putExtra("id",id)
         intent.putExtra("name",name)
         intent.putExtra("image",image)
         intent.putExtra("myToken",myToken)
         val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
             PendingIntent.FLAG_ONE_SHOT)

         val channelId = "Default"
         val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
         val notificationBuilder = NotificationCompat.Builder(this, channelId)
             .setSmallIcon(R.drawable.ic_logo)
             .setContentTitle(title)
             .setContentText(messageBody)
             .setPriority(NotificationCompat.PRIORITY_MAX)
             .setAutoCancel(true)
             .setSound(defaultSoundUri)
             .setContentIntent(pendingIntent)

         val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

         // Since android Oreo notification channel is needed.
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channel = NotificationChannel(channelId,
                 "Channel human readable title",
                 NotificationManager.IMPORTANCE_DEFAULT)
             notificationManager.createNotificationChannel(channel)
         }

         notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
     }
     */

    /* override fun handleIntent(intent: Intent) {
         super.handleIntent(intent)
         if (intent.getStringExtra("badge") == null) {
             Log.i("withnull", "iam null")
         } else {
             ShortcutBadger.applyCount(this, intent.getStringExtra("badge")!!.toInt())
         }

     }*/

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }

}