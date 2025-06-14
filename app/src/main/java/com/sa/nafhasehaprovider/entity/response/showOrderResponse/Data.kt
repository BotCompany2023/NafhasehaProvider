package com.sa.nafhasehaprovider.entity.response.showOrderResponse

import androidx.annotation.Keep
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Category
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Provider
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.Service
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.entity.response.ordersResponse.CanceledBy
import com.sa.nafhasehaprovider.entity.response.ordersResponse.Report
import com.sa.nafhasehaprovider.entity.response.ordersResponse.User
@Keep
data class Data(
    val canceled_by: CanceledBy? =null,
    val address: String? =null,
    val address_to: String? =null,
    val canceled_type: String? =null,
    val category: Category? =null,
    val city: CityResponseData? =null,
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
    val estimated_time: String? =null,
    val payment_method: String? =null,
    val position: String? =null,
    val provider: Provider? =null,
    val reason: String? =null,
    val service: Service? =null,
    val status: String? =null,
    val is_report: Boolean? =null,
    val sub_category: String? =null,
    val suggested_price: String? =null,
    val price_type: Int? =null,
    val is_price_request: Int? =null,
    val price_request: Int? =null,
    val time_at: String? =null,
    val transaction_id: Int? =null,
    val type: String? =null,
    val type_battery: TypeBattery? =null,
    val type_from: String? =null,
    val type_from_value: String? =null,
    val type_gasoline: String? =null,
    val is_offer_price: Int? =null,
    val user: User? =null,
    val user_vehicle: UserVehicle? =null,
    val report: Report? =null,
    val vehicle_transporter: VehicleTransporter? =null
)