package  com.sa.nafhasehaprovider.entity.response.showPackageResponse

data class ShowPackageResponse(
    val code: Int,
    val `data`: DataShowPackageResponse,
    val message: String,
    val status: String
)