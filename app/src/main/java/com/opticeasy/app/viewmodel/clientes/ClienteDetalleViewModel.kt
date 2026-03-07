package com.opticeasy.app.viewmodel.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteUpdateRequestDto
import com.opticeasy.app.data.repository.ClientesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ClienteDetalleState {
    data object Idle : ClienteDetalleState()
    data object Loading : ClienteDetalleState()
    data class Success(val cliente: ClienteDto) : ClienteDetalleState()
    data class Saved(val message: String = "Guardado") : ClienteDetalleState()
    data class Error(val message: String) : ClienteDetalleState()
}

class ClienteDetalleViewModel(
    private val repo: ClientesRepository = ClientesRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<ClienteDetalleState>(ClienteDetalleState.Idle)
    val state: StateFlow<ClienteDetalleState> = _state

    fun cargar(idCliente: Int) {
        viewModelScope.launch {
            try {
                _state.value = ClienteDetalleState.Loading
                val cliente = repo.obtenerClientePorId(idCliente)
                _state.value = ClienteDetalleState.Success(cliente)
            } catch (e: Exception) {
                _state.value = ClienteDetalleState.Error(e.message ?: "Error cargando cliente")
            }
        }
    }

    fun guardar(idCliente: Int, req: ClienteUpdateRequestDto) {
        viewModelScope.launch {
            try {
                _state.value = ClienteDetalleState.Loading
                repo.actualizarCliente(idCliente, req)
                // recargar para mostrar datos frescos
                val cliente = repo.obtenerClientePorId(idCliente)
                _state.value = ClienteDetalleState.Success(cliente)
            } catch (e: Exception) {
                _state.value = ClienteDetalleState.Error(e.message ?: "Error guardando cliente")
            }
        }
    }
}

