package com.sa.nafhasehaprovider.entity.response.conversationModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

import java.io.Serializable
@Keep
class Yourinfo : Serializable {
    @SerializedName("is_provider")
    var is_provider: Boolean = false
    @SerializedName("have_invoice")
    var have_invoice: Boolean = true
    @SerializedName("long")
    var lng: Double = 0.toDouble()
    @SerializedName("lat")
    var lat: Double = 0.toDouble()
    @SerializedName("name")
    var name: String? = null
    @SerializedName("id")
    var id: Int = 0

    fun isIs_provider(): Boolean {
        return is_provider
    }
}
