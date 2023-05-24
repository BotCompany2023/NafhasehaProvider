package  com.sa.nafhasehaprovider.entity.response.typeCarResponse


data class CarTypeResponse(
    val code: Int,
    val `data`: List<DataCarTypeResponse>? =null,
    val message: String,
    val status: String
)