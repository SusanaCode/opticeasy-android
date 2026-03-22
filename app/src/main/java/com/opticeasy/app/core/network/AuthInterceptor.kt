package com.opticeasy.app.core.network

import android.content.Context
import com.opticeasy.app.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
) : Interceptor {
    // SessionManager se crea una sola vez (no en cada intercepción)
    val sessionManager = SessionManager(context)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Acceso síncrono desde la caché en memoria — no bloquea
        val token = sessionManager.tokenSync

        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
