package com.sa.nafhasehaprovider.entity.response.carBrandsResponse

data class CarBrandsResponse(
    val code: Int,
    val `data`: List<DataCarBrandsResponse>? =null,
    val message: String,
    val status: String
)