package com.sa.nafhasehaprovider.entity.response.versionUpdateResponse

import androidx.annotation.Keep

@Keep

data class VersionUpdateResponse(
    val code: Int,
    val `data`: Boolean,
    val message: String,
    val status: String
)