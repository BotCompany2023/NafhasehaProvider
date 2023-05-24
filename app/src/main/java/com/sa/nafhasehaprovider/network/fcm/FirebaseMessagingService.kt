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

        Log.i(TAG, "wsltttt ")
        // Check if message contains a data payload.
//        Log.i(TAG, "Message data payload: " + remoteMessage.data)
        if (remoteMessage.data.isNotEmpty()) {
            Log.i(TAG, "wsltttt  data ")

//            Log.i("TestNotification" , "data")
//
//            Log.i("notificationpayload" , "data")
//
//            Log.i("Test","remoteMessage.data"+remoteMessage.data.toString() )
//            Log.i(TAG, "Message data payload: " + remoteMessage.data)
            try {
                Log.i(TAG, "wsltttt  data ")
                val params = remoteMessage.data
                val body = JSONObject(params as Map<*, *>)
                redirectId = remoteMessage.data.get("redirect_id")!!.toInt()
                Log.i(TAG, remoteMessage.data.get("redirect_id").toString())
                notificationId = body.getString("notification_id").toInt()
//                badge = body.getString("badge").toInt()
//                title = body.getString("title").toString()
//                nBody = body.getString("body").toString()

                var intent: Intent
                if (body.getString("click_action") == "1") {
                    Log.i(
                        TAG,
                        " data  click action =1 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "2") {
                    Log.i(
                        TAG,
                        " data  click action =2 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "3") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "4") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "5") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "11") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "6") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "8") {
                    Log.i(
                        TAG,
                        " data  click action =3 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "9") {
                    Log.i(
                        TAG,
                        " data  click action =9 , redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "10") {
                    Log.i(
                        TAG,
                        " data  click action =10, redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                // balance

                else if (body.getString("click_action") == "7") {
                    Log.i(
                        TAG,
                        " data  click action 7, redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
//                    intent.putExtra("redirect_id" ,body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "12") {
                    Log.i(
                        TAG,
                        " data  click action =10, redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "13") {
                    Log.i(
                        TAG,
                        " data  click action =13, redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "14") {
                    Log.i(
                        TAG,
                        " data  click action =14, redirectid string ${body.getString("redirect_id")} redirectid int ${
                            body.getInt("redirect_id")
                        } "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else if (body.getString("click_action") == "30") {
                    Log.i(
                        TAG,
                        " data  click action =${body.getString("click_action")}, redirectid string ${
                            body.getString("redirect_id")
                        } redirectid int ${body.getInt("redirect_id")} "
                    )
                    intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("click_action", body.getString("click_action"))
                    intent.putExtra("redirect_id", body.getString("redirect_id"))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } else {
                    intent = Intent(this, MainActivity::class.java)
                }

                // sending notification
                val pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                val channelId = "Default"
                val builder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_tool_bar)
                        .setContentTitle(remoteMessage.notification!!.title)
                        .setContentText(remoteMessage.notification!!.body).setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Default channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    manager.createNotificationChannel(channel)
                } else {

                }
                manager.notify(0, builder.build())

//                manager.notify(body.getString("click_action").toInt(),builder.build())


            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }


        // notification payload

        // Check if message contains a notification payload.


        if (remoteMessage.notification != null) {

            Log.i(
                TAG,
                " notification , redirectid ${remoteMessage.data.get("redirect_id")!!} click action ${remoteMessage.notification!!.clickAction}"
            )
            val click_action = remoteMessage.notification!!.clickAction //get click_action
            var redirectId = remoteMessage.data.get("redirect_id")!!
            var notification = remoteMessage.data.get("notification_id")!!

            var intent: Intent
//            intent = Intent(this, ChatActivity::class.java)
            Log.i("NotificationPayload", click_action!! + ",," + redirectId + ",," + notification)

            if (click_action == "1") {
                Log.i(TAG, " notification click action =1 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "4") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "5") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "11") {
                Log.i(TAG, "click action =4 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "3") {
                Log.i(TAG, "click action =3 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            } else if (click_action == "2") {
                Log.i(TAG, " notification click action =2 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "6") {
                Log.i(TAG, " notification click action =6 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "8") {
                Log.i(TAG, " notification click action =6 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "9") {
                Log.i(TAG, " notification click action =9 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "10") {
                Log.i(TAG, " notification click action =10 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "7") {
                Log.i(TAG, " notification click action =7 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "12") {
                Log.i(TAG, " notification click action =12 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "13") {
                Log.i(TAG, " notification click action =13 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "14") {
                Log.i(TAG, " notification click action =12 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else if (click_action == "30") {
                Log.i(TAG, " notification click action =30 , redirectid ${redirectId}")
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("redirect_id", redirectId)
                intent.putExtra("click_action", click_action)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else {
                intent = Intent(this, Notification::class.java)
                intent.putExtra("notificationId", notification.toInt())
                intent.putExtra("title", title)
                intent.putExtra("body", nBody)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            // sending notification
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = "Default"
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_tool_bar)
                    .setContentTitle(remoteMessage.notification!!.title)
                    .setContentText(remoteMessage.notification!!.body).setAutoCancel(true)
                    .setContentIntent(pendingIntent)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Default channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
            } else {

            }

            manager.notify(0, builder.build())

//            manager.notify(click_action.toInt(),builder.build())


            /*  if (click_action == "30" ||click_action == "31"||click_action == "32" ){
                  manager.notify(redirectId.toInt(), builder.build())

              }else {
                  manager.notify(notificationId, builder.build())
              }
  */
//            manager.notify(notificationId, builder.build())

        }

    }

    /* private fun sendNotification(messageBody: String,title: String,notification_id: String,
                                  click_action: String,redirect_id: String,id: String,name: String
                                  ,image: String,myToken: String) {
         val intent = Intent(this, HomeActivity::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         intent.putExtra("body",messageBody)
         intent.putExtra("title",title)
         intent.putExtra("click_action",click_action)
         intent.putExtra("notification_id",notification_id)
         intent.putExtra("redirect_id",redirect_id)
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