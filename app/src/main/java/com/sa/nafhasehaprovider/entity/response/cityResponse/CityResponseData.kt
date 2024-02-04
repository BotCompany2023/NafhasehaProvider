package  com.sa.nafhasehaprovider.entity.response.cityResponse

import androidx.annotation.Keep

@Keep
data class CityResponseData(
    val country: Country?,
    val id: Int,
    val title: String,

    )