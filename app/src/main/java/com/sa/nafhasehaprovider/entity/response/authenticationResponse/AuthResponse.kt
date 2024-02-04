package com.sa.nafhasehaprovider.entity.response.authenticationResponse

import androidx.annotation.Keep

@Keep
data class AuthResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)