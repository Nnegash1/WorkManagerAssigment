package com.example.workmanagerhomework1.domains.models

data class UploadResponse(
    val `data`: Data,
    val status: Int,
    val success: Boolean
)