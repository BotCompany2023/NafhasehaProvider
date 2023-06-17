package com.sa.nafhasehaprovider.entity.response.walletResponse

data class WalletResponse(
    val code: Int,
    val `data`: DataWalletResponse,
    val message: String,
    val status: String
)