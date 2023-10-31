package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleType

data class UserVehicle(
    val first_image: String? =null,
    val id: Int,
    val letters_ar: String? =null,
    val letters_en: String? =null,
    val numbers_ar: String? =null,
    val numbers_en: String? =null,
    val periodic_inspection: String? =null,
    val status: Int? =null,
    val vehicle_manufacture_year: VehicleManufactureYear? =null,
    val vehicle_model: VehicleModel? =null,
    val vehicle_type: VehicleType? =null
)