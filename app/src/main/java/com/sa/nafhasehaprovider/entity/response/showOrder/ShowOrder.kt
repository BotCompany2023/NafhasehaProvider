package com.sa.nafhasehaprovider.entity.response.showOrder

data class ShowOrder(
    val code: Int,
    val `data`: DataShowOrder,
    val message: String,
    val status: String
)