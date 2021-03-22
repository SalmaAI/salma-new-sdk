package com.banking.core.remote


import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.utils.DefaultIfNullFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@JvmField
val remoteModule = module {

    single<Moshi> {
        Moshi.Builder()
            .add(DefaultIfNullFactory())
            .add(KotlinJsonAdapterFactory())
            .build()
        //adapter
    }
    single {
        AuthorizationInterceptor()
    }

    single {
        AppAuthenticator()
    }

    //create OkHttpClient
    single {
        val builder = OkHttpClient.Builder()
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .connectTimeout(30, TimeUnit.SECONDS)

        builder.addInterceptor(get<AuthorizationInterceptor>())
        builder.authenticator(get<AppAuthenticator>())

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logger)
        }
        builder.build()
    }

    //Create retrofit builder
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .client(get<OkHttpClient>())
            .build()
    }

    //Create an implementation of the API endpoints
    single<ApiEndpoints> {
        get<Retrofit>().create(ApiEndpoints::class.java)
    }
}