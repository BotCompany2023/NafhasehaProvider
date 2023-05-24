package  com.sa.nafhasehaprovider.entity.response.walletResponse


data class DataWalletResponse(
    val my_wallet: Int,
    val invite_code: String,
    val transactions: List<TransactionWalletResponse>? =null
)