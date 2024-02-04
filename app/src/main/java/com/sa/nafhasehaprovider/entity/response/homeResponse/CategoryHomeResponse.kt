package com.sa.nafhasehaprovider.entity.response.homeResponse

import androidx.annotation.Keep


@Keep
data class CategoryHomeResponse(
    val id: Int,
    val image: String,
    val services: List<ServiceHomeResponse>,
    val title: String
)