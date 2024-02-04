package com.sa.nafhasehaprovider.entity.response.checkCouponResponse

import androidx.annotation.Keep

@Keep
data class CheckCouponResponse(
    val code: Int,
    val `data`: Data? = null,
    val message: String,
    val status: String
)