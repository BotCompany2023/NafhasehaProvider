package com.sa.nafhasehaprovider.entity.response.getBanksResponse

import androidx.annotation.Keep

@Keep
data class GetBanksResponse(
    val code: Int,
    val `data`: List<GetBanksResponseData>,
    val message: String,
    val status: String
)