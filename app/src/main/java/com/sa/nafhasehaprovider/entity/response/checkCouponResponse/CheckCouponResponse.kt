package com.sa.nafhasehaprovider.entity.response.checkCouponResponse

data class CheckCouponResponse(
    val code: Int,
    val `data`: Data? = null,
    val message: String,
    val status: String
)