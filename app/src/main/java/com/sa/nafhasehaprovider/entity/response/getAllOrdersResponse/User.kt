package com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Area
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.City
import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class User(
    val address: String,
    val area: Area,
    val city: City,
    val country: Country,
    val email: String,
    val id: Int,
    val image: String,
    val is_activation: Boolean,
    val lang: String,
    val lat: String,
    val name: String,
    val phone: String
)