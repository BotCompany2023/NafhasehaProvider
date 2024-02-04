package com.sa.nafhasehaprovider.entity.response.carBrandsResponse

import androidx.annotation.Keep

@Keep
data class CarBrandsResponse(
    val code: Int,
    val `data`: List<DataCarBrandsResponse>? =null,
    val message: String,
    val status: String
)