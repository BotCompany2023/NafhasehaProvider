package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import android.hardware.Camera
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponseData
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.entity.response.cityResponse.Country

data class Provider(
    val address: String? =null,
    val area: AreasResponseData? =null,
    val categories: List<Category>? =null,
    val city: CityResponseData? =null,
    val commercial_register: String? =null,
    val country: Country? =null,
    val default_language: String? =null,
    val email: String? =null,
    val id: Int,
    val image: String? ="",
    val is_activation: Boolean? =null,
    val is_active: Int? =null,
    val is_notification: Int? =null,
    val lat: String? =null,
    val long: String? =null,
    val is_rate: Boolean? =null,
    val name: String? =null,
    val phone: String? =null,
    val provider_type: String? =null,
    val services_from_home: Int? =null,
    val transporter_id: Int? =null
)