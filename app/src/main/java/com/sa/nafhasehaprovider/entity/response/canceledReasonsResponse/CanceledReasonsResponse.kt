package com.sa.nafhaseha.entity.response.canceledReasonsResponse

data class CanceledReasonsResponse(
    val code: Int,
    val `data`: List<DataCanceledReasonsResponse>,
    val message: String,
    val status: String
)