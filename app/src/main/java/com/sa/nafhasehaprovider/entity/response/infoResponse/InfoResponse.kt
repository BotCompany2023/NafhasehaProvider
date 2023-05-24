package  com.sa.nafhasehaprovider.entity.response.infoResponse


data class InfoResponse(
    val code: Int,
    val `data`: InfoResponseData,
    val message: String,
    val status: String
)