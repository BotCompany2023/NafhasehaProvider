package com.sa.nafhasehaprovider.entity.response.ordersResponse

import androidx.annotation.Keep

@Keep
data class VehicleTransporter(
    val id: Int,
    val image: String,
    val price: Int,
    val title: String
)