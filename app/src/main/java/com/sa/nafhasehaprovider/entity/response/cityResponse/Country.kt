package  com.sa.nafhasehaprovider.entity.response.cityResponse

import androidx.annotation.Keep

@Keep
data class Country(
    val code: String,
    val count: Int,
    val id: Int,
    val img: String,
    val title: String
)