package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import androidx.annotation.Keep

@Keep
data class Category(
    val id: Int,
    val image: String,
    val services: List<Service>,
    val title: String,
)