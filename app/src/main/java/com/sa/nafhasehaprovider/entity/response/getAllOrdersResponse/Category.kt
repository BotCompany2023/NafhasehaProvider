package  com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse

import com.sa.nafhasehaprovider.entity.response.homeResponse.ServiceHomeResponse


data class Category(
    val id: Int,
    val image: String,
    val services: List<ServiceHomeResponse>,
    val title: String
)