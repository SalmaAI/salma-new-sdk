package ai.mawdoo3.salma.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newBuilder = original.newBuilder()
        if (original.header("Content-Type") == null) {
            newBuilder.addHeader("Content-Type", "multipart/form-data")
        }
        newBuilder.addHeader("accept", "application/json")
//        newBuilder.addHeader(
//            "Authorization",
//            SecurityUtils.rsaEncrypt(
//                "4Eyugb2qVEYcQOyOCUYqmNY8xO8I7W7xf9kaJYlpI8ER7kOnVo0Z3CustWiO0Phtm5OBqMhOU8WLbajvttLv98s6c1ww0TDUKWvD6Jy6f-bh1o_LrewgKNF6o5qjeXjovkl1vjk0mgAHif88d6RRaYsBRIKbwC53Zi9KspKrF1Y",
//                context
//            )
//        )
        newBuilder.method(original.method, original.body)
        val request = newBuilder.build()
        return chain.proceed(request)
    }
}