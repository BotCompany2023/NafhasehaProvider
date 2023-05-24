package  com.sa.nafhasehaprovider.entity.response.providesMapResponse

data class ProvidesMapResponse(
    val code: Int,
    val `data`:List<DataProvidesMapResponse>? =null,
    val message: String,
    val status: String
)