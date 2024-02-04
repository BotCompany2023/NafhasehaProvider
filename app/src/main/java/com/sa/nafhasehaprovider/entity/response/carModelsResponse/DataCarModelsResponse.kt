package com.sa.nafhasehaprovider.entity.response.carModelsResponse

import androidx.annotation.Keep
import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleBrand

@Keep
data class DataCarModelsResponse(
    val id: Int,
    val title: String,
    val vehicle_brand: VehicleBrand? =null
)