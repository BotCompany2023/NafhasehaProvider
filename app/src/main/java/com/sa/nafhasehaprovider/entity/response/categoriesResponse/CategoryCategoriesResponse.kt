package com.sa.nafhasehaprovider.entity.response.categoriesResponse

import androidx.annotation.Keep

@Keep
data class CategoryCategoriesResponse(
    val id: Int,
    val image: String,
    val services: List<Service>,
    val title: String,
    var isSelected: Boolean? = false,
    var selectedItem: Boolean? = false
)