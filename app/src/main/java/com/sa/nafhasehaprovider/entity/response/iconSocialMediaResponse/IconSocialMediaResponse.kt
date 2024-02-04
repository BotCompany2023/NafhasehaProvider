package  com.sa.nafhasehaprovider.entity.response.iconSocialMediaResponse

import androidx.annotation.Keep

@Keep
data class IconSocialMediaResponse(
    val code: Int,
    val `data`: List<DataIconSocialMediaResponse>? =null,
    val message: String,
    val status: String
)