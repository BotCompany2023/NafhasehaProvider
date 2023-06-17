package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class Provider(
    val address: String? =null,
    val area: Area? =null,
    val categories: List<Category>? =null,
    val city: City? =null,
    val commercial_register: String? =null,
    val country: Country? =null,
    val email: String? =null,
    val id: Int? =null,
    val image: String? ="",
    val is_activation: Boolean? =null,
    val is_active: Int? =null,
    val lat: String? =null,
    val long: String? =null,
    val name: String? =null,
    val phone: String? =null,
    val provider_type: String? =null,
    val services_from_home: Int? =null,
)