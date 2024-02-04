package com.sa.nafhasehaprovider.entity.response.canceledReasonsResponse

import androidx.annotation.Keep

@Keep
data class CanceledReasonsResponse(
    val code: Int,
    val `data`: List<DataCanceledReasonsResponse>,
    val message: String,
    val status: String
)