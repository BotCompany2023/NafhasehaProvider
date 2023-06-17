package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class City(
    val country: Country,
    val id: Int,
    val title: String
)