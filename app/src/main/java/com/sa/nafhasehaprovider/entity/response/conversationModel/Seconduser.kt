package com.sa.nafhasehaprovider.entity.response.conversationModel

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Seconduser : Serializable {
    @SerializedName("avatar")
    var avatar: String? = null
    @SerializedName("rate")
    var rate: Float = 0.toFloat()
    @SerializedName("long")
    var lng: Double = 0.toDouble()
    @SerializedName("lat")
    var lat: Double = 0.toDouble()
    @SerializedName("delegate")
    var delegate: String? = null
    @SerializedName("phone")
    var phone: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("id")
    var id: Int = 0

    fun setRate(rate: Int) {
        this.rate = rate.toFloat()
    }
}
