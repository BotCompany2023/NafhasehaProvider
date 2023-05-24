package com.sa.nafhasehaprovider.entity.response.authenticationResponse

data class Data(
    val access_token: String,
    val expires_in: String? =null,
    val token_type: String,
    val user: User? =null
)