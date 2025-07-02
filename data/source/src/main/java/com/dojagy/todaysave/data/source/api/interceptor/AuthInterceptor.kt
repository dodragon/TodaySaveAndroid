package com.dojagy.todaysave.data.source.api.interceptor

import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.source.datastore.repo.TokenDatastoreRepositoryImpl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenRepo: TokenDatastoreRepositoryImpl
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.header("No-Auth") == "true") {
            return chain.proceed(request)
        }

        val accessToken = tokenRepo.currentToken?.accessToken

        if (!accessToken.isNullOrBlank() && accessToken != String.DEFAULT) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(request)
    }
}