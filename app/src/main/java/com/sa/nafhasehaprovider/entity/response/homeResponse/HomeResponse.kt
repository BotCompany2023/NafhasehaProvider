package com.sa.nafhasehaprovider.entity.response.homeResponse

data class HomeResponse(
    val code: Int,
    val `data`: DataHomeResponse,
    val message: String,
    val status: String
)