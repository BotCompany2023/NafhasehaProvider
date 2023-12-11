package com.sa.nafhasehaprovider.entity.response.areasResponse

import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData

data class AreasResponseData(
    val city: CityResponseData?,
    val id: Int,
    val title: String
)