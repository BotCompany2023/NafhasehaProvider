package  com.sa.nafhasehaprovider.entity.response.providerDataResponse

data class ProviderDataResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)