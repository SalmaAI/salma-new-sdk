package ai.mawdoo3.salma.module


import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.remote.AppAuthenticator
import ai.mawdoo3.salma.remote.AuthorizationInterceptor
import ai.mawdoo3.salma.remote.MasaApiEndpoints
import ai.mawdoo3.salma.utils.DefaultIfNullFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@JvmField
val remoteModule = module {

    single<Moshi>(named("masaMoshi")) {
        Moshi.Builder()
            .add(DefaultIfNullFactory())
            .add(KotlinJsonAdapterFactory())
            .build()
        //adapter
    }
    single(named("masaAuthInterceptor")) {
        AuthorizationInterceptor(get())
    }

    single(named("masaAuth")) {
        AppAuthenticator()
    }

    //create OkHttpClient
    single(named("masaOkhttpClient")) {
        val builder = OkHttpClient.Builder()
            .readTimeout(6, TimeUnit.MINUTES)
            .writeTimeout(6, TimeUnit.MINUTES)
            .connectTimeout(6, TimeUnit.MINUTES)
            .callTimeout(6, TimeUnit.MINUTES)

        builder.addInterceptor(get<AuthorizationInterceptor>(named("masaAuthInterceptor")))
        builder.authenticator(get<AppAuthenticator>(named("masaAuth")))

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logger)
        }
        builder.build()
    }

    //Create retrofit builder
    single<Retrofit>(named("masaRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BOT_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>(named("masaMoshi"))))
            .client(get<OkHttpClient>(named("masaOkhttpClient")))
            .build()
    }

    //Create an implementation of the API endpoints
    single<MasaApiEndpoints>(named("masaApiEndpoints")) {
        get<Retrofit>(named("masaRetrofit")).create(MasaApiEndpoints::class.java)
    }
}