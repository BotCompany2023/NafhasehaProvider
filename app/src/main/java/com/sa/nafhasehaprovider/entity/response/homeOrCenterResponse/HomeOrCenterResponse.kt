package  com.sa.nafhasehaprovider.entity.response.homeOrCenterResponse

data class HomeOrCenterResponse(
    val code: Int,
    val `data`: List<DataHomeOrCenterResponse>?=null,
    val message: String,
    val status: String
)