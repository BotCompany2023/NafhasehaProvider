package  com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse

data class VehicleTransporterResponse(
    val code: Int,
    val `data`: List<DataVehicleTransporterResponse>? =null,
    val message: String,
    val status: String
)