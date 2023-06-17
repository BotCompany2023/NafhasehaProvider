package  com.sa.nafhasehaprovider.entity.response.notificationResponse

data class NotificationResponse(
    val code: Int,
    val `data`: List<NotificationResponseData>? =null,
    val message: String,
    val status: String
)