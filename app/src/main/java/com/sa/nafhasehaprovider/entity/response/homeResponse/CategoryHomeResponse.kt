package com.sa.nafhasehaprovider.entity.response.homeResponse

data class CategoryHomeResponse(
    val id: Int,
    val image: String,
    val services: List<ServiceHomeResponse>,
    val title: String
)