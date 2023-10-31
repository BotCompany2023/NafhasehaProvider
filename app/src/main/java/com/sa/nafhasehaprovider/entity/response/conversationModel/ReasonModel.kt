package com.sa.nafhasehaprovider.entity.response.conversationModel

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * < Vampire >
 */

class ReasonModel : Serializable {

    @SerializedName("reason")
    var reason: String? = null
    @SerializedName("id")
    var id: Int = 0

    var isSelect: Boolean = false
}
