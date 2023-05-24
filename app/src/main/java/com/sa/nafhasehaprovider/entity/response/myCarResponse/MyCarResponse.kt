package  com.sa.nafhasehaprovider.entity.response.myCarResponse

data class MyCarResponse(
    val code: Int,
    val `data`: List<DataMyCarResponse>? =null,
    val message: String,
    val status: String
)