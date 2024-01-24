package com.sa.nafhasehaprovider.entity.response.categoriesResponse

data class Data(
    val categories: List<CategoryCategoriesResponse>? =null,
    val cost_maintenance: Int
)