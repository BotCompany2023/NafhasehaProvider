package  com.sa.nafhasehaprovider.entity.response.providerDataResponse

import androidx.annotation.Keep

@Keep
data class ServiceProviderDataResponse(
    val id: Int,
    val image: String,
    val title: String
)