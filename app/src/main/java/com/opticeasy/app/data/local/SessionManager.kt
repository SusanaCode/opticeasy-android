package com.opticeasy.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_ID_USUARIO = longPreferencesKey("id_usuario")
        private val KEY_NOMBRE = stringPreferencesKey("nombre_usuario")
        private val KEY_CODIGO_CENTRO = stringPreferencesKey("codigo_centro")
        private val KEY_ROL = stringPreferencesKey("rol")
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_ADMIN_USUARIOS = intPreferencesKey("admin_usuarios")
        private val KEY_LAST_ACTIVITY = longPreferencesKey("last_activity")
    }

    suspend fun saveUser(
        idUsuario: Long,
        nombre: String,
        codigoCentro: String,
        rol: String,
        token: String,
        adminUsuarios: Int
    ) {
        val now = System.currentTimeMillis()

        context.dataStore.edit { prefs ->
            prefs[KEY_ID_USUARIO] = idUsuario
            prefs[KEY_NOMBRE] = nombre
            prefs[KEY_CODIGO_CENTRO] = codigoCentro
            prefs[KEY_ROL] = rol
            prefs[KEY_TOKEN] = token
            prefs[KEY_ADMIN_USUARIOS] = adminUsuarios
            prefs[KEY_LAST_ACTIVITY] = now
        }
    }

    val idUsuario: Flow<Long?> =
        context.dataStore.data.map { it[KEY_ID_USUARIO] }

    val codigoCentro: Flow<String?> =
        context.dataStore.data.map { it[KEY_CODIGO_CENTRO] }

    val rol: Flow<String?> =
        context.dataStore.data.map { it[KEY_ROL] }

    val token: Flow<String?> =
        context.dataStore.data.map { it[KEY_TOKEN] }

    val adminUsuarios: Flow<Int> =
        context.dataStore.data.map { it[KEY_ADMIN_USUARIOS] ?: 0 }

    val lastActivity: Flow<Long> =
        context.dataStore.data.map { it[KEY_LAST_ACTIVITY] ?: 0L }

    suspend fun updateLastActivity() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_ACTIVITY] = System.currentTimeMillis()
        }
    }

    suspend fun isSessionExpired(timeoutMillis: Long): Boolean {
        val id = idUsuario.first()
        if (id == null || id == 0L) return false

        val last = lastActivity.first()
        if (last == 0L) return false

        return System.currentTimeMillis() - last > timeoutMillis
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

