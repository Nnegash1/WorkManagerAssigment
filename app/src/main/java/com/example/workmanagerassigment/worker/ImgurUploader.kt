package com.example.workmanagerhomework1.worker

import android.net.Uri
import com.example.workmanagerhomework1.data.remote.ImgurApiService
import com.example.workmanagerhomework1.domains.models.UploadResponse
import com.example.workmanagerhomework1.util.Resource
import java.io.File

interface ImgurUploader {
    val imgurApi: ImgurApiService
    suspend fun uploadFile(uri: Uri, title: String? = null): Resource<UploadResponse>
    fun copyStreamToFile(uri: Uri): File
}