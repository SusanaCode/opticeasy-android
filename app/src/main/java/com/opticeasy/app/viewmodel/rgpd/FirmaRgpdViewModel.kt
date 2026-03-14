package com.opticeasy.app.viewmodel.rgpd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.rgpd.FirmaRgpdRequestDto
import com.opticeasy.app.data.repository.RgpdRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class FirmaRgpdUiState {
    data object Idle : FirmaRgpdUiState()
    data object Loading : FirmaRgpdUiState()
    data class Success(val message: String) : FirmaRgpdUiState()
    data class Error(val message: String) : FirmaRgpdUiState()
}

class FirmaRgpdViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo = RgpdRepository(application.applicationContext)

    private val _state = MutableStateFlow<FirmaRgpdUiState>(FirmaRgpdUiState.Idle)
    val state: StateFlow<FirmaRgpdUiState> = _state

    fun reset() {
        _state.value = FirmaRgpdUiState.Idle
    }

    fun setError(msg: String) {
        _state.value = FirmaRgpdUiState.Error(msg)
    }

    fun guardarFirma(clienteId: Int, imagenBase64Png: String) {
        if (clienteId <= 0) {
            setError("ID cliente inválido")
            return
        }

        if (imagenBase64Png.isBlank()) {
            setError("La firma está vacía")
            return
        }

        _state.value = FirmaRgpdUiState.Loading

        val hoyIso = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val req = FirmaRgpdRequestDto(
            fecha_firma = hoyIso,
            imagen_firma = imagenBase64Png
        )

        viewModelScope.launch {
            try {
                repo.guardarFirma(clienteId, req)
                _state.value = FirmaRgpdUiState.Success("Firma guardada correctamente.")
            } catch (e: HttpException) {
                _state.value = FirmaRgpdUiState.Error("Error guardando firma (${e.code()})")
            } catch (e: Exception) {
                _state.value = FirmaRgpdUiState.Error("Error de red o inesperado")
            }
        }
    }
}

