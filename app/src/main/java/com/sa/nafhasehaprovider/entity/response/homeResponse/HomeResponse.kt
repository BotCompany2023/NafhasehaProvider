package com.sa.nafhasehaprovider.entity.response.homeResponse

import androidx.annotation.Keep


@Keep
data class HomeResponse(
    val code: Int,
    val `data`: DataHomeResponse? =null,
    val message: String,
    val status: String
)