package com.sa.nafhasehaprovider.entity.response.areasResponse

import androidx.annotation.Keep
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData

@Keep
data class AreasResponseData(
    val city: CityResponseData?,
    val id: Int,
    val title: String
)