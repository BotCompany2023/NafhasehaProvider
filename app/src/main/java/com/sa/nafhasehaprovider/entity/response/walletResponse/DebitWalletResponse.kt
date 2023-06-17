package com.sa.nafhasehaprovider.entity.response.walletResponse

data class DebitWalletResponse(
    val date: String,
    val final_total: Double,
    val id: Int,
    val invoice_no: String,
    val time: String,
    val title: String,
    val type: String,
    val type_id: Int
)