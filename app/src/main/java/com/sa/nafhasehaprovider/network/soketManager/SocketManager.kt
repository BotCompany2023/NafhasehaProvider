package com.sa.nafhasehaprovider.network.soketManager

import android.app.Activity
import android.util.Log


import com.google.gson.Gson
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.EmitKeyWord
import com.sa.nafhasehaprovider.common.SOCKET_URL
import com.sa.nafhasehaprovider.common.USER_DATA
import com.sa.nafhasehaprovider.common.onPrintLog
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder
import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerLocation
import com.sa.nafhasehaprovider.interfaces.NewOrder

import org.json.JSONObject

import java.net.URISyntaxException

import io.socket.client.IO
import io.socket.client.Socket

class SocketManager {

    var dataNewOrder: NewOrder? = null

    //  AppController appController;
    var mSocket: Socket? = null
    var mListener: SocketManagerListener? = null
    /**
     * connect to socket
     */

    var id= NafhasehaProviderApp.pref.loadUserData(
        NafhasehaProviderApp.context!!, USER_DATA
    )!!.data!!.provider!!.id!!



    fun connectToSocket() {

        Log.e(TAG, "Try Connecting to socket")

        try {
            if (mSocket != null) {
                Log.e(TAG, "disconnect to connect new socket")
                mSocket!!.close()
                mSocket!!.disconnect()
                mSocket = null
            }
            if (mSocket == null) {
                Log.e(TAG, "Socket equal null")
                Log.e(TAG, "Full socket url:" + SOCKET_URL)
                val opts = IO.Options()
                opts.forceNew = true
                mSocket = IO.socket("http://135.181.122.201:3000?type=Proviedr&id=${id}")
                mSocket!!.connect()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            socketFailedDialog()
            return
        }

        if (mListener != null) mListener!!.onLoginWithSocket()
        mSocket!!.on(Socket.EVENT_CONNECT) {
            if (mListener != null) mListener!!.onConnect()
        }
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) { socketFailedDialog() }
        mSocket!!.on(Socket.EVENT_DISCONNECT) { socketFailedDialog() }


        mSocket!!.on(EmitKeyWord.NEW_ORDER) { args ->
            val newMessage = args[0].toString()
            Log.e("sssswwq", "ORDER RECEIVEDzzz:" + Gson().toJson(newMessage))
            try {
                val message = Gson().fromJson(newMessage, GetNewOrder::class.java)
                if (dataNewOrder != null) {
                    dataNewOrder?.newOrder(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }






//    fun onGetLiveMessage() {
//        mSocket?.on(EmitKeyWord.RECIVE_MESSAGE) { args ->
//            val newMessage = args[0].toString()
//            onPrintLog(newMessage)
//
//            val gson = Gson()
//            try {
//                val message = gson.fromJson(newMessage, Messages::class.java)
//                if (mListener != null) mListener?.onMessageReceived(message)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


    protected fun socketFailedDialog() {
        Log.e(TAG, "socket faild connection")
        if (mListener != null) mListener!!.onSocketFailed()
    }

    /**
     * emit message to socket
     *
     * @param emitType   type of emit message
     * @param jsonObject data for send to server
     */
    fun emitMessage(emitType: String, jsonObject: JSONObject) {
        if (mSocket != null) {
            mSocket?.emit(emitType, jsonObject)
            Log.d("ssssffdfef","jjjj")
        } else {
            connectToSocket()
            Log.d("ssssffdfef2","jjjj")

        }
    }


    /**
     * close socket and disconnect to socket and set socket and listener to null
     */
    fun closeAndDisconnectSocket() {
        if (mSocket != null) {
            Log.e(TAG, "Closing socket")
            mSocket!!.close()
            mSocket!!.disconnect()
            mSocket = null
            mListener = null
        }
    }
    /**
     * reconnect to socket if socket is null or disconnected
     */
    fun tryToReconnect() {
        Log.e(TAG, "Check for socket reconnect")
        if (mSocket != null) {
            if (mSocket!!.connected()) {
            } else {
                Log.e(TAG, "soket is not connected")
                connectToSocket()
            }
        } else {
            Log.e(TAG, "m socket is empty Try to connect to socket")
            connectToSocket()
        }
    }

    companion object {
        var TAG = "SocketManager"
        private var socketManager: SocketManager? = null

        val instance: SocketManager
            get() {
                if (socketManager == null) {
                    socketManager = SocketManager()
                }
                return socketManager!!
            }
    }

}
