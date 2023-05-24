package com.sa.nafhasehaprovider.entity.response.areasResponse

import com.sa.nafhasehaprovider.entity.response.authenticationResponse.City

data class AreasResponseData(
    val city: City?,
    val id: Int,
    val title: String
)