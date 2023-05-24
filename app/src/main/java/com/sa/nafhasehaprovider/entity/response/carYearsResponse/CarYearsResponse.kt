package com.sa.nafhasehaprovider.entity.response.carYearsResponse

data class CarYearsResponse(
    val code: Int,
    val `data`: List<DataCarYearsResponse>,
    val message: String,
    val status: String
)