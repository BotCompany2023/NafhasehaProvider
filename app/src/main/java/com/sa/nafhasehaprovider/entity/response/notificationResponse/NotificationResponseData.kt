package  com.sa.nafhasehaprovider.entity.response.notificationResponse

import androidx.annotation.Keep

@Keep
data class NotificationResponseData(
    val body: String? =null,
    val id: Int? =null,
    val image: String? =null,
    val is_show: Int? =null,
    val title: String? =null,
    val type: String? =null,
    val type_id: Int? =null
)