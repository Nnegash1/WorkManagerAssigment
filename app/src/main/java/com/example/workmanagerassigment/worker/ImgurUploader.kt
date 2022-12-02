package com.example.workmanagerassigment.worker

import android.net.Uri
import com.example.workmanagerassigment.remote.ImgurApiService
import com.example.workmanagerassigment.models.UploadResponse
import com.example.workmanagerassigment.util.Resource
import java.io.File

interface ImgurUploader {
    val imgurApi: ImgurApiService
    suspend fun uploadFile(uri: Uri, title: String? = null): Resource<UploadResponse>
    fun copyStreamToFile(uri: Uri): File
}