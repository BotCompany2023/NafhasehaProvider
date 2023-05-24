package  com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse


data class GetAllPendingResponse(
    val code: Int,
    val `data`: List<GetAllPendingResponseData>? =null,
    val message: String,
    val status: String
)