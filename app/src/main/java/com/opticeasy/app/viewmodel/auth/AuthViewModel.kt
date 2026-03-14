package com.opticeasy.app.viewmodel.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.local.SessionManager
import com.opticeasy.app.data.remote.dto.auth.RegisterRequestDto
import com.opticeasy.app.data.remote.dto.auth.UsuarioDto
import com.opticeasy.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val usuario: UsuarioDto) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo = AuthRepository(application.applicationContext)
    private val sessionManager = SessionManager(application.applicationContext)

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            _state.value = AuthUiState.Error("Rellena login y contraseña")
            return
        }

        _state.value = AuthUiState.Loading

        viewModelScope.launch {
            try {
                val response = repo.login(login.trim(), password)
                val usuario = response.user

                sessionManager.saveUser(
                    idUsuario = usuario.idUsuario.toLong(),
                    nombre = "${usuario.nombreUsuario} ${usuario.apellidosUsuario}",
                    codigoCentro = usuario.codigoCentro,
                    rol = usuario.rol,
                    token = response.token
                )

                _state.value = AuthUiState.Success(usuario)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error("Credenciales incorrectas o error de red")
            }
        }
    }

    fun register(
        nombreUsuario: String,
        apellidosUsuario: String,
        nickUsuario: String,
        email: String,
        password: String,
        codigoCentro: String,
        rol: String,
        numeroColegiado: String?
    ) {
        if (
            nombreUsuario.isBlank() ||
            apellidosUsuario.isBlank() ||
            nickUsuario.isBlank() ||
            email.isBlank() ||
            password.isBlank() ||
            codigoCentro.isBlank() ||
            rol.isBlank()
        ) {
            _state.value = AuthUiState.Error("Faltan campos obligatorios")
            return
        }

        val colegiadoInt: Int? =
            if (rol == "optico") numeroColegiado?.toIntOrNull() else null

        _state.value = AuthUiState.Loading

        viewModelScope.launch {
            try {
                val usuario = repo.register(
                    RegisterRequestDto(
                        nombre_usuario = nombreUsuario,
                        apellidos_usuario = apellidosUsuario,
                        nick_usuario = nickUsuario,
                        email = email,
                        numero_colegiado = colegiadoInt,
                        password = password,
                        codigo_centro = codigoCentro,
                        rol = rol
                    )
                )

                sessionManager.saveUser(
                    idUsuario = usuario.idUsuario.toLong(),
                    nombre = "${usuario.nombreUsuario} ${usuario.apellidosUsuario}",
                    codigoCentro = usuario.codigoCentro,
                    rol = usuario.rol,
                    token = ""
                )

                _state.value = AuthUiState.Success(usuario)
            } catch (e: Exception) {
                _state.value = AuthUiState.Error(
                    "No se pudo crear el usuario (email, nick o colegiado duplicado, o error de red)"
                )
            }
        }
    }

    fun resetError() {
        if (_state.value is AuthUiState.Error) _state.value = AuthUiState.Idle
    }

    fun resetState() {
        _state.value = AuthUiState.Idle
    }
}



