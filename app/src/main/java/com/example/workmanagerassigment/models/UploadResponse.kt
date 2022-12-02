package com.example.workmanagerassigment.models

data class UploadResponse(
    val `data`: Data,
    val status: Int,
    val success: Boolean
)