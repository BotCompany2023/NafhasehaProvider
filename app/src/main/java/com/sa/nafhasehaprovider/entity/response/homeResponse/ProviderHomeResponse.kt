package com.sa.nafhasehaprovider.entity.response.homeResponse

data class ProviderHomeResponse(
    val avg_rate: String,
    val count_orders_completed: Int,
    val id: Int,
    val image: String,
    val is_activation: Boolean,
    val is_active: Int,
    val name: String,
    val rates_count: Int
)