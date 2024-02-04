package com.sa.nafhasehaprovider.entity.response.conversationModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * < Vampire >
 */
@Keep
class ReasonModel : Serializable {

    @SerializedName("reason")
    var reason: String? = null
    @SerializedName("id")
    var id: Int = 0

    var isSelect: Boolean = false
}
