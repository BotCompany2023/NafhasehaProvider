package com.sa.nafhasehaprovider.entity.response.cityResponse

data class CityResponse(
    val code: Int,
    val `data`: List<CityResponseData>,
    val message: String,
    val status: String
)