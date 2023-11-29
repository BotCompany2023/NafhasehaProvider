package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.CanceledBy

data class ShowOrderResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)