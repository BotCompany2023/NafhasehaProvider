package  com.sa.nafhasehaprovider.entity.response.infoResponse

import androidx.annotation.Keep


@Keep
data class InfoResponse(
    val code: Int,
    val `data`: InfoResponseData,
    val message: String,
    val status: String
)