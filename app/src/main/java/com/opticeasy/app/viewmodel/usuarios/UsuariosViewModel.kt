package com.opticeasy.app.viewmodel.usuarios

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.auth.UsuarioDto
import com.opticeasy.app.data.repository.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UsuariosUiState {
    data object Idle : UsuariosUiState()
    data object Loading : UsuariosUiState()
    data class Success(val usuarios: List<UsuarioDto>) : UsuariosUiState()
    data class Error(val message: String) : UsuariosUiState()
}

class UsuariosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuariosRepository(application)

    private val _uiState = MutableStateFlow<UsuariosUiState>(UsuariosUiState.Idle)
    val uiState: StateFlow<UsuariosUiState> = _uiState

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun cargarUsuarios() {
        viewModelScope.launch {
            _uiState.value = UsuariosUiState.Loading
            try {
                val usuarios = repository.listarUsuarios()
                _uiState.value = UsuariosUiState.Success(usuarios)
            } catch (e: Exception) {
                _uiState.value = UsuariosUiState.Error(
                    e.message ?: "Error cargando usuarios"
                )
            }
        }
    }

    fun cambiarPassword(idUsuario: Int, nuevaPassword: String) {
        if (nuevaPassword.length < 6) {
            _mensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        viewModelScope.launch {
            try {
                repository.cambiarPasswordUsuario(idUsuario, nuevaPassword)
                _mensaje.value = "Contraseña actualizada correctamente"
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "Error cambiando contraseña"
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun cambiarActivo(idUsuario: Int, activoActual: Int) {
        val nuevoActivo = if (activoActual == 1) 0 else 1

        viewModelScope.launch {
            try {
                repository.cambiarActivoUsuario(idUsuario, nuevoActivo)
                _mensaje.value = if (nuevoActivo == 1) {
                    "Usuario activado correctamente"
                } else {
                    "Usuario desactivado correctamente"
                }
                cargarUsuarios()
            } catch (e: Exception) {
                _mensaje.value = e.message ?: "Error cambiando el estado del usuario"
            }
        }
    }
}

