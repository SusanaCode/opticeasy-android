package com.opticeasy.app.viewmodel.revisiones

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaDetalleDto
import com.opticeasy.app.data.repository.RevisionesGafaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetalleRevisionGafaState {
    data object Idle : DetalleRevisionGafaState()
    data object Loading : DetalleRevisionGafaState()
    data class Success(val revision: RevisionGafaDetalleDto) : DetalleRevisionGafaState()
    data class Error(val message: String) : DetalleRevisionGafaState()
}

class DetalleRevisionGafaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RevisionesGafaRepository(application)

    private val _state = MutableStateFlow<DetalleRevisionGafaState>(DetalleRevisionGafaState.Idle)
    val state: StateFlow<DetalleRevisionGafaState> = _state

    fun cargar(idRevision: Long) {
        viewModelScope.launch {
            _state.value = DetalleRevisionGafaState.Loading
            try {
                val revision = repo.obtenerRevisionGafaPorId(idRevision)
                _state.value = DetalleRevisionGafaState.Success(revision)
            } catch (_: Exception) {
                _state.value = DetalleRevisionGafaState.Error(
                    "No se pudo cargar el detalle de la revisión de gafa."
                )
            }
        }
    }
}

