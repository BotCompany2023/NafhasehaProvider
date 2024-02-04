package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import androidx.annotation.Keep

@Keep

data class ShowOrderResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)