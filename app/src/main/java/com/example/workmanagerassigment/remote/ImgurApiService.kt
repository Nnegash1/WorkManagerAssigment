package com.example.workmanagerassigment.remote

import com.example.workmanagerassigment.models.UploadResponse
import com.example.workmanagerassigment.util.Constants
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurApiService {
    @Multipart
    @POST(Constants.UPLOAD_ENDPOINT)
    suspend fun uploadImage(
        @Part image: MultipartBody.Part?
    ): Response<UploadResponse>

    companion object {
        fun getInstance() = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(
                        LocalInterceptor()
                    )
                    .build()
            )
            .build()
            .create<ImgurApiService>()
    }

}