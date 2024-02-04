package  com.sa.nafhasehaprovider.entity.response.providerDataResponse

import androidx.annotation.Keep

@Keep
data class CategoryProviderDataResponse(
    val id: Int,
    val image: String,
    val services: List<ServiceProviderDataResponse>? =null,
    val title: String
)