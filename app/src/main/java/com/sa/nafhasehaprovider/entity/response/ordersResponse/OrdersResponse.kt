package com.sa.nafhasehaprovider.entity.response.ordersResponse

data class OrdersResponse(
    val code: Int,
    val `data`: List<DataOrdersResponse>? =null,
    val message: String,
    val status: String
)