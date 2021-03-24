package ai.mawdoo3.salma.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppAuthenticator() :
    Authenticator,
    KoinComponent {
    val apiEndpoints: ApiEndpoints by inject()

    //The authenticate() method is called when server returns 401 Unauthorized.
    override fun authenticate(route: Route?, response: Response): Request? {
        runBlocking {
            return@runBlocking null


        }
        return null
    }
}