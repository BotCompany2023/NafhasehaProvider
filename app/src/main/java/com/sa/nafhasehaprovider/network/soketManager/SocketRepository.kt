package com.sa.nafhasehaprovider.network.soketManager

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerLocation
import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.sa.nafhasehaprovider.common.EmitKeyWord
import com.sa.nafhasehaprovider.common.onConvertObjToJson
import com.sa.nafhasehaprovider.entity.request.acceptOrder.AcceptOrderRequest
import com.sa.nafhasehaprovider.entity.request.sendOffer.SendOfferRequest
import com.sa.nafhasehaprovider.entity.response.conversationModel.ConversationModel
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder
import com.sa.nafhasehaprovider.interfaces.NewOrder
import com.sa.nafhasehaprovider.interfaces.SuccessEmit

/**
< Vampire >
 */
@SuppressLint("StaticFieldLeak")
object SocketRepository : SocketManagerListener, NewOrder, SuccessEmit {
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
        socketManager?.successEmit = this
    }


    fun onAcceptOrderRequest(model: AcceptOrderRequest) {
        socketManager?.emitMessage(
            EmitKeyWord.ACCEPTED_ORDER,
            onConvertObjToJson(model)!!)

        return
    }

    fun onSendOfferRequest(model: SendOfferRequest) {
        socketManager?.emitMessage(
            EmitKeyWord.SEND_OFFER_ORDER,
            onConvertObjToJson(model)!!)

        return
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

    override fun successEmit(success: Boolean) {

    }


}