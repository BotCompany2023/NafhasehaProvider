package com.sa.nafhasehaprovider.entity.response.countNotificationResponse

import androidx.annotation.Keep

@Keep
data class CountNotificationResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val status: String
)