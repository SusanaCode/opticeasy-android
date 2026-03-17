

package com.opticeasy.app.viewmodel.revisiones

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcDetalleDto
import com.opticeasy.app.data.repository.RevisionesLcRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetalleRevisionLcState {
    data object Idle : DetalleRevisionLcState()
    data object Loading : DetalleRevisionLcState()
    data class Success(val revision: RevisionLcDetalleDto) : DetalleRevisionLcState()
    data class Error(val message: String) : DetalleRevisionLcState()
}

class DetalleRevisionLcViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RevisionesLcRepository(application)

    private val _state = MutableStateFlow<DetalleRevisionLcState>(DetalleRevisionLcState.Idle)
    val state: StateFlow<DetalleRevisionLcState> = _state

    fun cargar(idRevision: Long) {
        viewModelScope.launch {
            _state.value = DetalleRevisionLcState.Loading
            try {
                val revision = repo.obtenerRevisionLcPorId(idRevision)
                _state.value = DetalleRevisionLcState.Success(revision)
            } catch (_: Exception) {
                _state.value = DetalleRevisionLcState.Error(
                    "No se pudo cargar el detalle de la revisión de lentes de contacto."
                )
            }
        }
    }
}