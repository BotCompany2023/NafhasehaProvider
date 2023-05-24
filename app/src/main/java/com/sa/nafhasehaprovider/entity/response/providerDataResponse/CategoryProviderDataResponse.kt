package  com.sa.nafhasehaprovider.entity.response.providerDataResponse


data class CategoryProviderDataResponse(
    val id: Int,
    val image: String,
    val services: List<ServiceProviderDataResponse>? =null,
    val title: String
)