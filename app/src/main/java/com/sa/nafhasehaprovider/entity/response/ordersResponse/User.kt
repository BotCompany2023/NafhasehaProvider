package com.sa.nafhasehaprovider.entity.response.ordersResponse

import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponseData
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class User(
    val address: String? =null,
    val area: AreasResponseData? =null,
    val city: CityResponseData? =null,
    val country: Country? =null,
    val default_language: String? =null,
    val email: String? =null,
    val id: Int,
    val image: String? ="",
    val is_activation: Boolean? =null,
    val is_notification: Int,
    val lang: String? =null,
    val lat: String? =null,
    val name: String? =null,
    val phone: String? =null
)