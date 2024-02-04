package com.sa.nafhasehaprovider.entity.response.walletResponse

import androidx.annotation.Keep

@Keep

data class WalletResponse(
    val code: Int,
    val `data`: DataWalletResponse,
    val message: String,
    val status: String
)