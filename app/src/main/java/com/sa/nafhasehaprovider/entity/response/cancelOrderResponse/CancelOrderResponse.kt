package com.sa.nafhasehaprovider.entity.response.cancelOrderResponse

data class CancelOrderResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val status: String
)