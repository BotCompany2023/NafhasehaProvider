package  com.sa.nafhasehaprovider.entity.response.providerDataResponse

import androidx.annotation.Keep

@Keep
data class ProviderDataResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)