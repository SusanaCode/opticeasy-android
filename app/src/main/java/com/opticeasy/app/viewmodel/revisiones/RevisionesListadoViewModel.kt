package com.opticeasy.app.viewmodel.revisiones

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaListItemDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcListItemDto
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
        val revisionesGafa: List<RevisionGafaListItemDto>,
        val revisionesLc: List<RevisionLcListItemDto>
    ) : RevisionesListadoState()

    data class Error(val message: String) : RevisionesListadoState()
}

class RevisionesListadoViewModel(application: Application) : AndroidViewModel(application) {

    private val repoGafa = RevisionesGafaRepository(application)
    private val repoLc = RevisionesLcRepository(application)

    private val _state = MutableStateFlow<RevisionesListadoState>(RevisionesListadoState.Idle)
    val state: StateFlow<RevisionesListadoState> = _state

    fun cargar(clienteId: Long) {
        viewModelScope.launch {
            try {
                _state.value = RevisionesListadoState.Loading

                val gafaDef = async { repoGafa.listarRevisionesGafa(clienteId) }
                val lcDef = async { repoLc.listarRevisionesLc(clienteId) }

                _state.value = RevisionesListadoState.Success(
                    revisionesGafa = gafaDef.await(),
                    revisionesLc = lcDef.await()
                )
            } catch (e: Exception) {
                _state.value = RevisionesListadoState.Error(
                    e.message ?: "Error cargando revisiones"
                )
            }
        }
    }
}

