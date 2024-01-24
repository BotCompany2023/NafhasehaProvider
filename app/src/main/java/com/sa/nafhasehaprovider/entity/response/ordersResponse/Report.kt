package com.sa.nafhasehaprovider.entity.response.ordersResponse

class Report(
    val id: Int,
    val order_service_id: Int,
    val price: String,
    val status: String? = null,
    val date_at: String,
    val time_at: String,
    val details: String,
    val images: List<String>
)