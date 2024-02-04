package com.sa.nafhasehaprovider.entity.response.homeResponse

import androidx.annotation.Keep

@Keep
data class ProviderHomeResponse(
    val avg_rate: String,
    val count_orders_completed: Int,
    val id: Int,
    val image: String? =null,
    val is_activation: Boolean,
    val is_active: Int,
    val name: String,
    val get_orders: Int,
    val rates_count: Int
)