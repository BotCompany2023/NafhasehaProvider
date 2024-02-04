package com.sa.nafhasehaprovider.entity.response.carBrandsResponse

import androidx.annotation.Keep
import com.sa.nafhasehaprovider.entity.response.myCarResponse.VehicleType

@Keep
data class DataCarBrandsResponse(
    val id: Int,
    val image: String? =null,
    val title: String? =null,
    val vehicle_type: VehicleType? =null
)