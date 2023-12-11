package com.sa.nafhasehaprovider.entity.response.authenticationResponse

data class AuthResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)