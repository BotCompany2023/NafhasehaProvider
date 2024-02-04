package com.sa.nafhasehaprovider.entity.response.addCarResponse

import androidx.annotation.Keep


@Keep
data class AddCarResponse(
    val code: Int,
    val `data`: DataAddCarResponse? =null,
    val message: String,
    val status: String


)