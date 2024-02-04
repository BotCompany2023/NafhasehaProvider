package com.sa.nafhasehaprovider.entity.response.conversationModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * < Vampire >
 */

@Keep
class ConversationModel : Serializable {

    @SerializedName("messages")
    var messages: List<Messages>? = null
        get() = if (field != null) {
            field
        } else {
            ArrayList()

        }
    @SerializedName("reasons")
    var reasons: List<ReasonModel>? = null
    @SerializedName("seconduser")
    var seconduser: Seconduser? = null
    @SerializedName("yourinfo")
    var yourinfo: Yourinfo? = null
//    @SerializedName("order")
//    var order: OrderDetailsModel? = null
    @SerializedName("using_hyperpay")
    var using_hyperpay: String? = "null"
}
