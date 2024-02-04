package com.sa.nafhasehaprovider.entity.response.areasResponse

import androidx.annotation.Keep

@Keep
data class AreasResponse(
    val code: Int,
    val `data`: List<AreasResponseData>,
    val message: String,
    val status: String
)