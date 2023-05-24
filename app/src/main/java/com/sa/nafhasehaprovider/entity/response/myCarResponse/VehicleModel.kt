package  com.sa.nafhasehaprovider.entity.response.myCarResponse

data class VehicleModel(
    val id: Int,
    val title: String,
    val vehicle_brand: VehicleBrand? =null
)