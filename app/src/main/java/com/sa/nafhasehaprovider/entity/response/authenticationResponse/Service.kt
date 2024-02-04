package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import androidx.annotation.Keep


@Keep
data class Service(
    val id: Int,
    val image: String,
    val title: String
)