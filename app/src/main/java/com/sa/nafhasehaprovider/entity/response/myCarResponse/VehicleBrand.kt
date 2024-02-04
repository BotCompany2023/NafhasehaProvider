package  com.sa.nafhasehaprovider.entity.response.myCarResponse

import androidx.annotation.Keep


@Keep
data class VehicleBrand(
    val id: Int,
    val image: ImageMyCarResponse? =null,
    val title: String? =null,
    val vehicle_type: VehicleType? =null
)