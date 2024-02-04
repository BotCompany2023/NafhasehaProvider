package  com.sa.nafhasehaprovider.entity.response.myCarResponse

import androidx.annotation.Keep

@Keep
data class VehicleModel(
    val id: Int,
    val title: String,
    val vehicle_brand: VehicleBrand? =null
)