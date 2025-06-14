package com.sa.nafhasehaprovider.entity.response.homeResponse

import androidx.annotation.Keep

@Keep
data class NewOrderHomeResponse(
    val address: String? =null,
    val address_to: String? =null,
    val canceled_by: String? =null,
    val canceled_type: String? =null,
    val category: CategoryHomeResponse? =null,
    val date_at:String? =null,
    val details: String? =null,
    val discount_amount: String? =null,
    val final_total: String,
    val suggested_price: String,
    val grand_total: String,
    val id: Int,
    val distance:  String? =null,
    val invoice_no: String? =null,
    val lat: String? =null,
    val lat_to: String? =null,
    val long: String? =null,
    val long_to: String? =null,
    val position: List<String>? =null,
    val provider: ProviderHomeResponse? =null,
    val reason: String? =null,
    val service: ServiceHomeResponse? =null,
    val status: String? =null,
    val time_at: String? =null,
    val transaction_id: Int? =null,
    val type: String? =null,
    val is_price_request: Int? =null,
    val price_request: Int? =null,
    val price_type: Int? =null,
    val type_from: String? =null,
    val is_offer_price: Int? =null,
    val type_from_value: String? =null
)