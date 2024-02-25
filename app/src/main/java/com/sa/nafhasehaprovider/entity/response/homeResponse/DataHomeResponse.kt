package com.sa.nafhasehaprovider.entity.response.homeResponse

import androidx.annotation.Keep


@Keep
data class DataHomeResponse(
    val new_orders: List<NewOrderHomeResponse>,
    val provider: ProviderHomeResponse,
    val my_wallet: String? =null,
    val is_have_order:Boolean?=false
)