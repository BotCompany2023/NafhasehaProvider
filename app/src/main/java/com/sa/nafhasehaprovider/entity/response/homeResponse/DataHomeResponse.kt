package com.sa.nafhasehaprovider.entity.response.homeResponse

data class DataHomeResponse(
    val new_orders: List<NewOrderHomeResponse>,
    val provider: ProviderHomeResponse,
    val my_wallet: String? =null
)