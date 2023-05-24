package com.sa.nafhasehaprovider.entity.response.areasResponse

data class AreasResponse(
    val code: Int,
    val `data`: List<AreasResponseData>,
    val message: String,
    val status: String
)