package com.sa.nafhasehaprovider.entity.response.homeResponse

import androidx.annotation.Keep

@Keep
data class ServiceHomeResponse(
    val id: Int,
    val image: String,
    val title: String
)