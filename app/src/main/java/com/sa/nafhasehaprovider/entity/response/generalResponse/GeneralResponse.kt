package  com.sa.nafhasehaprovider.entity.response.generalResponse

data class GeneralResponse(
    val code: Int,
    val `data`: Int? =null,
    val message: String,
    val status: String
)