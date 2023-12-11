package com.ksa.smartcarb.entity.response.versionUpdateResponse

data class VersionUpdateResponse(
    val code: Int,
    val `data`: Boolean,
    val message: String,
    val status: String
)