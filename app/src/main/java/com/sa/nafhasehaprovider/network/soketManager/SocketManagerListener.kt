package com.sa.nafhasehaprovider.network.soketManager

import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerLocation


interface SocketManagerListener {
    fun onConnect()
    fun onSocketFailed()
    fun onLoginWithSocket()
//    fun onMessageReceived(message: Messages)
    fun onSocketError(code: Int)
}
