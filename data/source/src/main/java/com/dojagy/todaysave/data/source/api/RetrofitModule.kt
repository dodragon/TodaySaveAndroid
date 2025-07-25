package com.dojagy.todaysave.data.source.api

import com.dojagy.todaysave.common.util.AppConfig
import com.dojagy.todaysave.data.source.BuildConfig
import com.dojagy.todaysave.data.source.api.adapter.LocalDateAdapter
import com.dojagy.todaysave.data.source.api.adapter.LocalDateTimeAdapter
import com.dojagy.todaysave.data.source.api.interceptor.AuthInterceptor
import com.dojagy.todaysave.data.source.api.interceptor.PrettyHttpLoggingInterceptor
import com.dojagy.todaysave.data.source.api.interceptor.TokenAuthenticator
import com.dojagy.todaysave.data.source.api.service.ContentService
import com.dojagy.todaysave.data.source.api.service.UserService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(PrettyHttpLoggingInterceptor()).apply {
            level = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            }else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(authInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        appConfig: AppConfig,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        return Retrofit.Builder()
                .baseUrl(appConfig.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(object : Converter.Factory() {
                    fun converterFactory() = this
                    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object :
                        Converter<ResponseBody, Any?> {
                        val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                        override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) {
                            try{
                                nextResponseBodyConverter.convert(value)
                            }catch (e:Exception){
                                e.printStackTrace()
                                null
                            }
                        } else{
                            null
                        }
                    }
                })
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }


    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideContentService(retrofit: Retrofit): ContentService =
        retrofit.create(ContentService::class.java)
}