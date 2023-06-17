package com.sa.nafhasehaprovider.entity.response.authenticationResponse

data class Data(
    val access_token: String? =null,
    val expires_in: String? =null,
    val token_type: String? =null,
    val provider: Provider? =null,
)