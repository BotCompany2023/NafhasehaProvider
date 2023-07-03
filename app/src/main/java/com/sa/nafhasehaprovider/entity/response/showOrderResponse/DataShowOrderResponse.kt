package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Category
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Service
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.User

data class DataShowOrderResponse(
    val address: String? =null,
    val address_to: String? =null,
    val canceled_by: String? =null,
    val canceled_type: String? =null,
    val category: Category,
    val date_at: String? =null,
    val details: String? =null,
    val discount_amount: String? =null,
    val distance: String? =null,
    val final_total: String? =null,
    val grand_total: String? =null,
    val id: Int,
    val images: List<String>? =null,
    val invoice_no: String? =null,
    val lat: String? =null,
    val lat_to: String? =null,
    val long: String? =null,
    val long_to: String? =null,
    val payment_type: String? =null,
    val position: List<String>? =null,
    val provider: String? =null,
    val reason: String? =null,
    val service: Service,
    val status: String? =null,
    val time_at: String? =null,
    val transaction_id: Int,
    val type: String? =null,
    val type_from: String? =null,
    val type_from_value: String? =null,
    val user: User,
    val vehicle_transporter: VehicleTransporter? =null,
    val videos: List<String>? =null
)