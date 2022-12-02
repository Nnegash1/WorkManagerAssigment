package com.example.workmanagerassigment.worker

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.workmanagerassigment.remote.ImgurApiService
import com.example.workmanagerassigment.models.UploadResponse
import com.example.workmanagerassigment.util.Constants
import com.example.workmanagerassigment.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class ImgurWorker(
    private val context: Context, workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork() = withContext(Dispatchers.IO) {
        return@withContext try {
            val imgurUriInput =
                inputData.getString(Constants.KEY_IMAGE_URI) ?: return@withContext Result.failure()
            val response = uploadFile(Uri.parse(imgurUriInput), null)
            when (response) {
                is Resource.Success -> {
                    val link = response.data.data.link
                    val outputData = workDataOf(Constants.KEY_IMAGE_URI to link)
                    seeImagePostedToImgur(outputData)
                    Result.success(outputData)
                }

                is Resource.Error -> {
                    Result.failure()
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun seeImagePostedToImgur(link: androidx.work.Data) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.getString(Constants.KEY_IMAGE_URI)))
        context.startActivity(intent)
    }

    private val imgurApi = ImgurApiService.getInstance()

    private suspend fun uploadFile(uri: Uri, title: String?): Resource<UploadResponse> {
        return try {
            val file = copyStreamToFile(uri)
            val requesetFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val filePart = MultipartBody.Part.createFormData("Image", file.name, requesetFile)
            val response = imgurApi.uploadImage(image = filePart)
            if (response.isSuccessful) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Something went wrong")
            }
        } catch (e: Exception) {
            Resource.Error("Something went wrong")
        }
    }

    private suspend fun copyStreamToFile(uri: Uri): File {
        val outputFile = File.createTempFile("temp", null)
        applicationContext.contentResolver
            .openInputStream(uri)?.use { input ->
                val outputStream = FileOutputStream(outputFile)
                outputStream.use { output ->
                    val buffer = ByteArray(4 * 1024)
                    while (true) {
                        val byteCount = input.read(buffer)
                        if (byteCount < 0) break
                        output.write(buffer, 0, byteCount)
                    }
                    output.flush()
                }
            }
        return outputFile
    }

}
