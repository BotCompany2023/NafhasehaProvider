package com.sa.nafhasehaprovider.entity.response.conversationModel

import android.util.Log

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sand.sa.data.Model.ConversationModel.GeoModel

import java.io.Serializable

class Messages : Serializable {

    @SerializedName("date")
    var date: String? = null
    @SerializedName("avatar")
    var avatar: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("content")
    var content: String? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("user_id")
    var user_id: Int = 0
    @SerializedName("sender")
    var sender: String? = null

    fun onGetGeoModel(): GeoModel {
        Log.e("msg3", Gson().toJson(content))
        return Gson().fromJson(content, GeoModel::class.java)
    }
}
