package com.example.ddtapp.di

import com.example.ddtapp.BuildConfig
import com.example.ddtapp.api.Api
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApi(
        retrofit: Retrofit
    ): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    internal fun provideRetrofit(
        okHttpBuilder: OkHttpClient.Builder,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpBuilder.build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    internal fun provideOkHttpClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
        return builder
    }

    @Singleton
    @Provides
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Singleton
    @Provides
    internal fun provideGson(): Gson = GsonBuilder()
        .serializeNulls()
        .create()
}