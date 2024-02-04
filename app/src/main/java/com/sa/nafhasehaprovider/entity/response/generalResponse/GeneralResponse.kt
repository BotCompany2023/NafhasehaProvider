package  com.sa.nafhasehaprovider.entity.response.generalResponse

import androidx.annotation.Keep

@Keep
data class GeneralResponse(
    val code: Int,
    val `data`: Int? =null,
    val message: String,
    val status: String
)