package com.sa.nafhasehaprovider.entity.response.showOrderResponse

data class ShowOrderResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)