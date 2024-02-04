package com.sa.nafhasehaprovider.entity.response.addCarResponse

import androidx.annotation.Keep
import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleBrand
import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleManufactureYear
import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleModel
import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleType

@Keep
data class DataAddCarResponse(
    val first_image: String,
    val id: Int,
    val letters_ar: String,
    val letters_en: String,
    val numbers_ar: String,
    val numbers_en: String,
    val periodic_inspection: String,
    val status: Int? =null,
    val vehicle_brand: VehicleBrand? =null,
    val vehicle_manufacture_year: VehicleManufactureYear? =null,
    val vehicle_model: VehicleModel? =null,
    val vehicle_type: VehicleType? =null
)