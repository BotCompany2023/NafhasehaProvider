package com.sa.nafhasehaprovider.entity.response.categoriesResponse

import androidx.annotation.Keep

@Keep
data class CategoriesResponse(
    val code: Int,
    val `data`: Data? =null,
    val message: String,
    val status: String
)