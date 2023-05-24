package com.sa.nafhasehaprovider.entity.response.authenticationResponse

data class User(
    val address: String? =null,
    val area: Area? =null,
    val categories: List<Category>? =null,
    val city: City? =null,
    val commercial_register: String? =null,
    val country: CountryXX? =null,
    val email: String? =null,
    val id: Int? =null,
    val image: String? =null,
    val is_activation: Boolean? =null,
    val is_active: Int? =null,
    val lat: String? =null,
    val long: String? =null,
    val name: String? =null,
    val phone: String? =null,
    val provider_type: String? =null,
    val services_from_home: Int? =null,
)