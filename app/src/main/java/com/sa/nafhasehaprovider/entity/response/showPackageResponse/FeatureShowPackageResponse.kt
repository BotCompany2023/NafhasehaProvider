package  com.sa.nafhasehaprovider.entity.response.showPackageResponse

import androidx.annotation.Keep

@Keep
data class FeatureShowPackageResponse(
    val description: String,
    val id: Int,
    val is_boolean: Int,
    val title: String,
    val value: String
)