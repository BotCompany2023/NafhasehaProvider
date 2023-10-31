package com.sa.nafhasehaprovider.network.soketManager

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerLocation
import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.common.EmitKeyWord
import com.sa.nafhasehaprovider.common.onConvertObjToJson
import com.sa.nafhasehaprovider.common.util.MapUtil.animateMarker
import com.sa.nafhasehaprovider.entity.response.conversationModel.ConversationModel
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder
import com.sa.nafhasehaprovider.interfaces.NewOrder
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository.marker
import com.sa.nafhasehaprovider.ui.activity.MainActivity

/**
< Vampire >
 */
@SuppressLint("StaticFieldLeak")
object SocketRepository : SocketManagerListener, NewOrder {
    var context: Context? = null
    var socketManager: SocketManager? = null
    val mediaPlayer = MediaPlayer()
    val mTracker = TrackerModel()
    var mConversationModel: ConversationModel? = null
//    var adapter: ChatAdapter? = null
    var map: GoogleMap? = null
    var marker: Marker? = null
//    var mPresenter: ChatPresenter? = null


    fun ConnectToSocket() {
        socketManager = SocketManager()
        socketManager?.tryToReconnect()
        socketManager?.mListener = this
        socketManager?.dataNewOrder = this
    }


    fun onSendLocation(model: TrackerLocation) {
        socketManager?.emitMessage(
            EmitKeyWord.UPDATELOCATOON,
            onConvertObjToJson(model)!!)

        return
    }

    fun onDisconnect() {
        socketManager?.closeAndDisconnectSocket()
    }

    override fun onConnect() {

    }


    override fun onSocketFailed() {
        //TODO
    }

    override fun onLoginWithSocket() {
        //TODO

    }

    override fun onSocketError(code: Int) {
        //TODO
    }

    override fun newOrder(model: GetNewOrder) {

    }


}