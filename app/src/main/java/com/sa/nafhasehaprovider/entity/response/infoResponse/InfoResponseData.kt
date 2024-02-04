package  com.sa.nafhasehaprovider.entity.response.infoResponse

import androidx.annotation.Keep

@Keep
data class InfoResponseData(
    val description: String,
    val id: Int,
    val title: String
)