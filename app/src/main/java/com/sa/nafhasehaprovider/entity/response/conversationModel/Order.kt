package com.sa.nafhasehaprovider.entity.response.conversationModel

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Order : Serializable {
    @SerializedName("details")
    var details: String? = null
    @SerializedName("price")
    var price: String? = null
    @SerializedName("deliver_address")
    var deliver_address: String? = null
    @SerializedName("deliver_long")
    var deliver_long: Double = 0.toDouble()
    @SerializedName("deliver_lat")
    var deliver_lat: Double = 0.toDouble()
    @SerializedName("receive_address")
    var receive_address: String? = null
    @SerializedName("receive_long")
    var receive_long: Double = 0.toDouble()
    @SerializedName("receive_lat")
    var receive_lat: Double = 0.toDouble()
    @SerializedName("deliver_time")
    var deliver_time: String? = null
    @SerializedName("place_icon")
    var place_icon: String? = null
    @SerializedName("place_name")
    var place_name: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("have_invoice")
    private var have_invoice: String? = null
    @SerializedName("id")
    var id: Int = 0


    fun getHave_invoice(): Boolean {
        return have_invoice == "true"
    }

    fun setHave_invoice(have_invoice: String) {
        this.have_invoice = have_invoice
    }
}
