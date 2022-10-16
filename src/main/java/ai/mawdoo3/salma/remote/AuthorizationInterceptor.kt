package ai.mawdoo3.salma.remote

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class AuthorizationInterceptor() : Interceptor,
    KoinComponent {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newBuilder = original.newBuilder()

//        val accessToken =
//            runBlocking(Dispatchers.IO) { authLocalDataSource.getAuthToken() }
//        accessToken?.let {
//            if (it.isNotEmpty()) {
//                newBuilder
//                    .addHeader("Authorization", "Bearer $it")
//            }
//        }


        if (original.header("Content-Type") == null) {
            newBuilder.addHeader("Content-Type", "multipart/form-data")
        }
        newBuilder.addHeader("accept", "application/json")
        newBuilder.method(original.method, original.body)
        val request = newBuilder.build()
        return chain.proceed(request)
    }
}