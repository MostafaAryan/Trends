package com.programmerofpersia.trends.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.ApiExecutorImpl
import com.programmerofpersia.trends.data.remote.Constants
import com.programmerofpersia.trends.data.remote.TrApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {

        /* todo if(appconfig.isdebug) */
        addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )

        addInterceptor { chain ->
            val response = chain.proceed(chain.request())
            val bodyString = response.body?.string()

            val newResponse = bodyString.run {
                if (!isNullOrEmpty() && contains(Constants.charsInApiResponseToBeRemoved)) {
                    val newBody = replace(Constants.charsInApiResponseToBeRemoved, "")
                        .toResponseBody(response.body?.contentType())

                    response.newBuilder().body(newBody).build()
                } else response
            }

            newResponse
        }

    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_TRENDS_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiExecutor(): ApiExecutor = ApiExecutorImpl()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): TrApi {
        return retrofit.create(TrApi::class.java)
    }

}