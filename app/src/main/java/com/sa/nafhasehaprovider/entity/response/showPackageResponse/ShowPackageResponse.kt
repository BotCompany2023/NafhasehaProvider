package  com.sa.nafhasehaprovider.entity.response.showPackageResponse

import androidx.annotation.Keep

@Keep
data class ShowPackageResponse(
    val code: Int,
    val `data`: DataShowPackageResponse,
    val message: String,
    val status: String
)