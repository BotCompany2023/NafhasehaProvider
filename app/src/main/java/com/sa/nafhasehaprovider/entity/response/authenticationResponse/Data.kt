package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import androidx.annotation.Keep

@Keep
data class Data(
    val access_token: String? =null,
    val expires_in: String? =null,
    val provider: Provider? =null,
    val token_type: String? =null
)