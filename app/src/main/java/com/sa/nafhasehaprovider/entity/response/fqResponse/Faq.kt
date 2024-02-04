package  com.sa.nafhasehaprovider.entity.response.fqResponse

import androidx.annotation.Keep

@Keep
data class Faq(
    val description: String,
    val id: Int,
    val title: String
)