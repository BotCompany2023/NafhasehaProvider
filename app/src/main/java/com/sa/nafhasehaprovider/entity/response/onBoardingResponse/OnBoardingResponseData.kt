package  com.sa.nafhasehaprovider.entity.response.onBoardingResponse

import androidx.annotation.Keep

@Keep
data class OnBoardingResponseData(
    val description: String,
    val id: Int,
    val image: String,
    val title: String
)