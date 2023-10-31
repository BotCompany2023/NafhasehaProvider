package com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse

data class AllOrdersResponse(
    val code: Int,
    val `data`: List<DataAllOrdersResponse>? =null,
    val message: String,
    val status: String
)