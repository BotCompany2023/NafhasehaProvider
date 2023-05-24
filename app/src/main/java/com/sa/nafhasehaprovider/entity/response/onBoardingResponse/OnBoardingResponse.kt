package  com.sa.nafhasehaprovider.entity.response.onBoardingResponse


data class OnBoardingResponse(
    val code: Int,
    val `data`: List<OnBoardingResponseData>,
    val message: String,
    val status: String
)