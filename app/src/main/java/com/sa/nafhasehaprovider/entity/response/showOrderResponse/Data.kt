package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Category
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.City
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Provider
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Service
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.User

data class Data(
    val address: String? =null,
    val address_to: String? =null,
    val canceled_by: String? =null,
    val canceled_type: String? =null,
    val category: Category? =null,
    val city: City? =null,
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
    val estimated_time: String? =null,
    val payment_method: String? =null,
    val position: String? =null,
    val provider: Provider? =null,
    val reason: String? =null,
    val service: Service? =null,
    val status: String? =null,
    val sub_category: String? =null,
    val suggested_price: String? =null,
    val time_at: String? =null,
    val transaction_id: Int? =null,
    val type: String? =null,
    val type_battery: TypeBattery? =null,
    val type_from: String? =null,
    val type_from_value: String? =null,
    val type_gasoline: String? =null,
    val user: User? =null,
    val user_vehicle: UserVehicle? =null,
    val vehicle_transporter: VehicleTransporter? =null
)