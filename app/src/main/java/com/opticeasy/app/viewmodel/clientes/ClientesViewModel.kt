package com.opticeasy.app.viewmodel.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateRequestDto
import com.opticeasy.app.data.repository.ClientesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class ClientesUiState {
    data object Idle : ClientesUiState()
    data object Loading : ClientesUiState()
    data class Success(val idCliente: Int) : ClientesUiState()
    data class Error(val message: String) : ClientesUiState()
}

class ClientesViewModel(
    private val repo: ClientesRepository = ClientesRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<ClientesUiState>(ClientesUiState.Idle)
    val state: StateFlow<ClientesUiState> = _state

    fun crearCliente(req: ClienteCreateRequestDto) {
        _state.value = ClientesUiState.Loading

        viewModelScope.launch {
            try {
                val res = repo.crearCliente(req)
                _state.value = ClientesUiState.Success(res.id_cliente)
            } catch (e: HttpException) {
                val msg = when (e.code()) {
                    409 -> "El DNI ya existe"
                    400 -> "Nombre, apellidos y DNI son obligatorios"
                    else -> "Error creando cliente (${e.code()})"
                }
                _state.value = ClientesUiState.Error(msg)
            } catch (e: Exception) {
                _state.value = ClientesUiState.Error("Error de red o inesperado")
            }
        }
    }

    fun resetState() {
        _state.value = ClientesUiState.Idle
    }
}

