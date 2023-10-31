package com.sa.nafhasehaprovider.entity.response.getNewOrder

data class ResponseNewOrder(
    val category_image: String,
    val category_name: List<CategoryName>? =null,
    val invoice_no: String,
    val is_offer_price: Int,
    val order_id: Int,
    val suggested_price: Double,
    val type: String,
    val user_id: Int
)