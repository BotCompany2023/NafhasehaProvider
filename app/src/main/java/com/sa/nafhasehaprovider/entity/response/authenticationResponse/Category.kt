package com.sa.nafhasehaprovider.entity.response.authenticationResponse

data class Category(
    val id: Int,
    val image: String,
    val services: List<Service>,
    val title: String
)