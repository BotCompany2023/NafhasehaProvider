package com.sa.nafhasehaprovider.entity.response.carModelsResponse

import androidx.annotation.Keep

@Keep
data class CarModelsResponse(
    val code: Int,
    val `data`: List<DataCarModelsResponse>? =null,
    val message: String,
    val status: String
)