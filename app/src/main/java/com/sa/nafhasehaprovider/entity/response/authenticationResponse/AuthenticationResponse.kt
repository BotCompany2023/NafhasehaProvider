package com.sa.nafhasehaprovider.entity.response.authenticationResponse

data class AuthenticationResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)