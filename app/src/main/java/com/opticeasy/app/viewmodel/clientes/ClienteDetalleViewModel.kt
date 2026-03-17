package com.opticeasy.app.viewmodel.clientes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    application: Application
) : AndroidViewModel(application) {

    private val repo = ClientesRepository(application.applicationContext)

    private val _state = MutableStateFlow<ClienteDetalleState>(ClienteDetalleState.Idle)
    val state: StateFlow<ClienteDetalleState> = _state

    fun cargar(idCliente: Int) {
        viewModelScope.launch {
            _state.value = ClienteDetalleState.Loading
            try {
                val cliente = repo.obtenerClientePorId(idCliente)
                _state.value = ClienteDetalleState.Success(cliente)
            } catch (_: Exception) {
                _state.value = ClienteDetalleState.Error(
                    "No se pudo cargar el cliente. Inténtalo de nuevo."
                )
            }
        }
    }

    fun guardar(idCliente: Int, req: ClienteUpdateRequestDto) {
        viewModelScope.launch {
            _state.value = ClienteDetalleState.Loading
            try {
                repo.actualizarCliente(idCliente, req)
                val cliente = repo.obtenerClientePorId(idCliente)
                _state.value = ClienteDetalleState.Success(cliente)
            } catch (_: Exception) {
                _state.value = ClienteDetalleState.Error(
                    "No se pudo guardar la ficha del cliente. Inténtalo de nuevo."
                )
            }
        }
    }
}

