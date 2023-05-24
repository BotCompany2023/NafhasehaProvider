package  com.sa.nafhasehaprovider.entity.response.showPackageResponse


data class DataShowPackageResponse(
    val features: List<FeatureShowPackageResponse>,
    val id: Int,
    val image: String,
    val price: String,
    val title: String
)