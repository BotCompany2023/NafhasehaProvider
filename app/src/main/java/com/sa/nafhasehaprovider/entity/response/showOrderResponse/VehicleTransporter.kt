package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import androidx.annotation.Keep

@Keep
data class VehicleTransporter(
    val id: Int,
    val image: String? =null,
    val price: Int? =null,
    val title: String? =null
)