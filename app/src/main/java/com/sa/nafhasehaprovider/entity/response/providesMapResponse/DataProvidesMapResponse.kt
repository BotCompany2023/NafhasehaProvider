package  com.sa.nafhasehaprovider.entity.response.providesMapResponse

import androidx.annotation.Keep

@Keep
data class DataProvidesMapResponse(
    val id: Int,
    val image: String,
    val lat: String,
    val long: String,
    val name: String,
    val provider_type: String
)