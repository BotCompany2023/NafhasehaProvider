package  com.sa.nafhasehaprovider.entity.response.fqResponse

import androidx.annotation.Keep

@Keep
data class FaqsResponseData(
    val faqs: List<Faq>,
    val id: Int,
    val title: String
)