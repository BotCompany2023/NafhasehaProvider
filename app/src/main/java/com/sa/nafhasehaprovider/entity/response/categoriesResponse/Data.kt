package com.sa.nafhasehaprovider.entity.response.categoriesResponse

import androidx.annotation.Keep

@Keep
data class Data(
    val categories: List<CategoryCategoriesResponse>? =null,
    val cost_maintenance: Int
)