package com.sa.nafhasehaprovider.entity.response.countNotificationResponse

data class CountNotificationResponse(
    val code: Int,
    val `data`: Int? =null,
    val message: String,
    val status: String
)