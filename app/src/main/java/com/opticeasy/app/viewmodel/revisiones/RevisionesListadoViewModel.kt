package com.opticeasy.app.viewmodel.revisiones

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcListItemDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaListItemDto
import com.opticeasy.app.data.repository.ClientesRepository
import com.opticeasy.app.data.repository.RevisionesGafaRepository
import com.opticeasy.app.data.repository.RevisionesLcRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RevisionesListadoState {
    data object Idle : RevisionesListadoState()
    data object Loading : RevisionesListadoState()

    data class Success(
        val cliente: ClienteDto,
        val revisionesGafa: List<RevisionGafaListItemDto>,
        val revisionesLc: List<RevisionLcListItemDto>
    ) : RevisionesListadoState()

    data class Error(val message: String) : RevisionesListadoState()
}

class RevisionesListadoViewModel(application: Application) : AndroidViewModel(application) {

    private val repoGafa = RevisionesGafaRepository(application)
    private val repoLc = RevisionesLcRepository(application)
    private val repoClientes = ClientesRepository(application.applicationContext)

    private val _state = MutableStateFlow<RevisionesListadoState>(RevisionesListadoState.Idle)
    val state: StateFlow<RevisionesListadoState> = _state

    fun cargar(clienteId: Long) {
        viewModelScope.launch {
            _state.value = RevisionesListadoState.Loading
            try {
                val clienteDef = async { repoClientes.obtenerClientePorId(clienteId.toInt()) }
                val gafaDef = async { repoGafa.listarRevisionesGafa(clienteId) }
                val lcDef = async { repoLc.listarRevisionesLc(clienteId) }

                _state.value = RevisionesListadoState.Success(
                    cliente = clienteDef.await(),
                    revisionesGafa = gafaDef.await(),
                    revisionesLc = lcDef.await()
                )
            } catch (_: Exception) {
                _state.value = RevisionesListadoState.Error(
                    "No se pudieron cargar las revisiones."
                )
            }
        }
    }
}

