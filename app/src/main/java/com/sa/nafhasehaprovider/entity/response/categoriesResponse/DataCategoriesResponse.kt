package com.sa.nafhasehaprovider.entity.response.categoriesResponse

data class DataCategoriesResponse(
    val id: Int,
    val image: String,
    val services: List<Service>,
    val title: String
)