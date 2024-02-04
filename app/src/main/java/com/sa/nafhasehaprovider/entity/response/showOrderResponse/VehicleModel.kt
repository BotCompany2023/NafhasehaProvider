package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import androidx.annotation.Keep

@Keep
data class VehicleModel(
    val id: Int,
    val title: String? =null,
    val vehicle_brand: VehicleBrand? =null
)