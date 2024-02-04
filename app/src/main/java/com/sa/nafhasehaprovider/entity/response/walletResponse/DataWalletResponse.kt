package com.sa.nafhasehaprovider.entity.response.walletResponse

import androidx.annotation.Keep

@Keep

data class DataWalletResponse(
    val credit: List<CreditWalletResponse>? =null,
    val debit: List<DebitWalletResponse>? =null,
    val my_wallet: Double? =null,
)