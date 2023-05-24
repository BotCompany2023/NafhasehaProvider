package  com.sa.nafhasehaprovider.entity.response.notificationResponse

data class NotificationResponseData(
    val body: String,
    val id: Int,
    val image: String,
    val is_show: Int,
    val title: String,
    val type: String,
    val type_id: Int
)