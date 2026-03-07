package com.opticeasy.app.viewmodel.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.data.repository.ClientesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ClientesBuscarState {
    data object Idle : ClientesBuscarState()
    data object Loading : ClientesBuscarState()
    data class Success(val clientes: List<ClienteDto>) : ClientesBuscarState()
    data class Error(val message: String) : ClientesBuscarState()
}

class ClientesBuscarViewModel(
    private val repo: ClientesRepository = ClientesRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<ClientesBuscarState>(ClientesBuscarState.Idle)
    val state: StateFlow<ClientesBuscarState> = _state

    fun buscar(
        nombre: String,
        apellidos: String,
        dni: String,
        telefono: String,
        codigoCliente: String
    ) {
        viewModelScope.launch {
            try {
                val cod = codigoCliente.trim()
                val n = nombre.trim()
                val a = apellidos.trim()
                val d = dni.trim()
                val t = telefono.trim()

                val hayFiltrosBusqueda =
                    n.isNotBlank() || a.isNotBlank() || d.isNotBlank() || t.isNotBlank()

                val hayCodigo = cod.isNotBlank()

                if (!hayFiltrosBusqueda && !hayCodigo) {
                    _state.value = ClientesBuscarState.Error("Rellena al menos un campo.")
                    return@launch
                }

                _state.value = ClientesBuscarState.Loading

                if (hayCodigo && !hayFiltrosBusqueda) {
                    val id = cod.toIntOrNull()
                        ?: throw IllegalArgumentException("El código cliente debe ser numérico.")

                    val cliente = repo.obtenerClientePorId(id)
                    _state.value = ClientesBuscarState.Success(listOf(cliente))
                    return@launch
                }


                val lista = repo.buscarClientes(
                    nombre = n,
                    apellidos = a,
                    dni = d,
                    telefono = t
                )


                val filtrada = if (hayCodigo) {
                    val id = cod.toIntOrNull()
                        ?: throw IllegalArgumentException("El código cliente debe ser numérico.")
                    lista.filter { it.idCliente == id }
                } else {
                    lista
                }

                _state.value = ClientesBuscarState.Success(filtrada)

            } catch (e: Exception) {
                _state.value = ClientesBuscarState.Error(
                    e.message ?: "Error buscando clientes"
                )
            }
        }
    }

    fun reset() {
        _state.value = ClientesBuscarState.Idle
    }
}




