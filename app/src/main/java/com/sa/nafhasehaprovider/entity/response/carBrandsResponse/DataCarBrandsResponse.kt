package com.sa.nafhasehaprovider.entity.response.carBrandsResponse

import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleType

data class DataCarBrandsResponse(
    val id: Int,
    val image: String? =null,
    val title: String? =null,
    val vehicle_type: VehicleType? =null
)