package com.sa.nafhasehaprovider.entity.response.addCarResponse

data class AddCarResponse(
    val code: Int,
    val `data`: DataAddCarResponse? =null,
    val message: String,
    val status: String


)