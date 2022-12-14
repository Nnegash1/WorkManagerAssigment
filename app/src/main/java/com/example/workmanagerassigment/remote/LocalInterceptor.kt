package com.example.workmanagerassigment.remote

import com.example.workmanagerassigment.util.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LocalInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request =
            chain.request().newBuilder()
                .addHeader("Authorization", "CLIENT-ID ${Constants.CLIENT_ID}")
                .build()
        return chain.proceed(request)
    }
}