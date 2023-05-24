package  com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse

import com.sa.nafhasehaprovider.entity.response.homeResponse.ServiceHomeResponse


data class GetAllPendingResponseData(
    val address: String? =null,
    val address_to: String? =null,
    val canceled_by: String? =null,
    val canceled_type: String? =null,
    val category: Category? =null,
    val date_at: String? =null,
    val details: String? =null,
    val discount_amount: String? =null,
    val final_total:  String? =null,
    val grand_total:  String? =null,
    val id: Int,
    val invoice_no:String? =null,
    val lat:  Double? =null,
    val lat_to:  Double? =null,
    val long:  Double? =null,
    val long_to:  Double? =null,
    val position: List<String>? =null,
    val provider:  String? =null,
    val reason:  String? =null,
    val service: ServiceHomeResponse? =null,
    val status:  String? =null,
    val time_at:  String? =null,
    val transaction_id: Int,
    val type:  String? =null,
    val type_from:  String? =null,
    val type_from_value: String? =null
)