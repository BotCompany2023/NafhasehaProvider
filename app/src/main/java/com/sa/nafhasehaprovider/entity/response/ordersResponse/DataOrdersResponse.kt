package com.sa.nafhasehaprovider.entity.response.ordersResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Category
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Provider
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Service
import com.sa.nafhasehaprovider.entity.response.showOrderResponse.Images

data class DataOrdersResponse(
    val address: String? =null,
    val address_to: String? =null,
    val canceled_by: CanceledBy? =null,
    val canceled_type: String? =null,
    val is_report: Boolean? =null,
    val report: Report? =null,
    val category: Category? =null,
    val date_at: String? =null,
    val details: String? =null,
    val discount_amount: String? =null,
    val distance: String? =null,
    val final_total: String? =null,
    val grand_total: String? =null,
    val id: Int,
    val images: List<Images>? =null,
    val invoice_no: String? =null,
    val lat: String? =null,
    val lat_to: String? =null,
    val long: String? =null,
    val long_to: String? =null,
    val payment_type: String? =null,
    val position: List<String>? =null,
    val provider: Provider? =null,
    val reason: String? =null,
    val service: Service? =null,
    val status: String? =null,
    val time_at: String? =null,
    val transaction_id: Int? =null,
    val type: String? =null,
    val type_from:String? =null,
    val type_from_value: String? =null,
    val user: User? =null,
    val vehicle_transporter: VehicleTransporter? =null,
    val videos: List<String>? =null
)