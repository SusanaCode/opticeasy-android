package com.opticeasy.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
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
    }

    suspend fun saveUser(
        idUsuario: Long,
        nombre: String,
        codigoCentro: String,
        rol: String,
        token: String,
        adminUsuarios: Int
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ID_USUARIO] = idUsuario
            prefs[KEY_NOMBRE] = nombre
            prefs[KEY_CODIGO_CENTRO] = codigoCentro
            prefs[KEY_ROL] = rol
            prefs[KEY_TOKEN] = token
            prefs[KEY_ADMIN_USUARIOS] = adminUsuarios
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

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

