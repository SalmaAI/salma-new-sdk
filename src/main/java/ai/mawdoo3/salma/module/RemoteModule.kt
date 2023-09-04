package ai.mawdoo3.salma.module


import ai.mawdoo3.salma.BuildConfig
import ai.mawdoo3.salma.remote.AuthorizationInterceptor
import ai.mawdoo3.salma.remote.MasaApiEndpoints
import ai.mawdoo3.salma.utils.DefaultIfNullFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


open class RemoteModule {

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BOT_API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(getMoshiBuilder()))
                .client(getOkHttpClientBuilder())

                .build()
        }
        return retrofit
    }

   private fun getMoshiBuilder():Moshi{
        return Moshi.Builder()
            .add(DefaultIfNullFactory())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun getOkHttpClientBuilder() : OkHttpClient{
        val builder = OkHttpClient.Builder()
            .readTimeout(6, TimeUnit.MINUTES)
            .writeTimeout(6, TimeUnit.MINUTES)
            .connectTimeout(6, TimeUnit.MINUTES)
            .callTimeout(6, TimeUnit.MINUTES)
        builder.addInterceptor(AuthorizationInterceptor())
        //builder.authenticator(get<AppAuthenticator>(named("masaAuth")))

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logger)
        }
        return builder.build()
    }

    fun getAPIServices() : MasaApiEndpoints? {
        return getRetrofitInstance()?.create(
            MasaApiEndpoints::class.java
        ) ?: null

    }



}