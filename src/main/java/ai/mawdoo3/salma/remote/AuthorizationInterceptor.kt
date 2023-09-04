package ai.mawdoo3.salma.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newBuilder = original.newBuilder()
        newBuilder.addHeader("accept", "application/json")
        newBuilder.method(original.method, original.body)
        val request = newBuilder.build()
        return chain.proceed(request)
    }
}