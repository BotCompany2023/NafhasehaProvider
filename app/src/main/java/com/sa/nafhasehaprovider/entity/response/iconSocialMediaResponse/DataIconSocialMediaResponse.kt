package  com.sa.nafhasehaprovider.entity.response.iconSocialMediaResponse

import androidx.annotation.Keep

@Keep
data class DataIconSocialMediaResponse(
    val id: Int,
    val image: String,
    val link: String,
    val title: String
)