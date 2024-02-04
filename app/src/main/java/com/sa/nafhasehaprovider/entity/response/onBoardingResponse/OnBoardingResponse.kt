package  com.sa.nafhasehaprovider.entity.response.onBoardingResponse

import androidx.annotation.Keep

@Keep
data class OnBoardingResponse(
    val code: Int,
    val `data`: List<OnBoardingResponseData>,
    val message: String,
    val status: String
)