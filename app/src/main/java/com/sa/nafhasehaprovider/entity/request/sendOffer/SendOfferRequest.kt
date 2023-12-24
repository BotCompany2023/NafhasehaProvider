package com.sa.nafhasehaprovider.entity.request.sendOffer

data class SendOfferRequest(
    val avg_rate: String,
    val image: String,
    val name: String,
    val number_phone: String,
    val order_id: Int,
    val price: Int,
    val type_provider: String,
    val price_type: Int
)