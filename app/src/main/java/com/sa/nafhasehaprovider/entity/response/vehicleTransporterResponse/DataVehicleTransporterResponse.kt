package  com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse

import androidx.annotation.Keep

@Keep

data class DataVehicleTransporterResponse(
    val id: Int,
    val image: String,
    val price: Int,
    val title: String,
    var isSelected: Boolean?=false
)