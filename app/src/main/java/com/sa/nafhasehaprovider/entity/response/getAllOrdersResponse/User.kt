package com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Area
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.City
import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class User(
    val address: String? =null,
    val area: Area? =null,
    val city: City? =null,
    val country: Country,
    val email: String? =null,
    val id: Int,
    val image: String? =null,
    val is_activation: Boolean,
    val lang: String? =null,
    val lat: String? =null,
    val name: String? =null,
    val phone: String? =null
)