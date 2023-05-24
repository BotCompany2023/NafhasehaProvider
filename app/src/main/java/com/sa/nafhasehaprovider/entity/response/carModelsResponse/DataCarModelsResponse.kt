package com.sa.nafhasehaprovider.entity.response.carModelsResponse

import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleBrand

data class DataCarModelsResponse(
    val id: Int,
    val title: String,
    val vehicle_brand: VehicleBrand? =null
)