package com.sa.nafhasehaprovider.entity.response.cancelOrderResponse

import androidx.annotation.Keep


@Keep
data class CancelOrderResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val status: String
)