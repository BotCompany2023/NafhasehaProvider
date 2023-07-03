package com.sa.nafhasehaprovider.entity.response.showOrderResponse

data class ShowOrderResponse(
    val code: Int,
    val `data`: DataShowOrderResponse? =null,
    val message: String,
    val status: String
)