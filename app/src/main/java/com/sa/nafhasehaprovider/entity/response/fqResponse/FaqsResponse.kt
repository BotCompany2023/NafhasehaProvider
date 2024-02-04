package  com.sa.nafhasehaprovider.entity.response.fqResponse

import androidx.annotation.Keep

@Keep
data class FaqsResponse(
    val code: Int,
    val `data`: List<FaqsResponseData>,
    val message: String,
    val status: String
)