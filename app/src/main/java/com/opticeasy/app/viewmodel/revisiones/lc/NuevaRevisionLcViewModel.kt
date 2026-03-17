package com.opticeasy.app.viewmodel.revisiones.lc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateRequestDto
import com.opticeasy.app.data.repository.ClientesRepository
import com.opticeasy.app.data.repository.RevisionesLcRepository
import com.opticeasy.app.ui.screens.revisiones.lc.RevisionLcFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class NuevaRevisionLcViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RevisionesLcRepository(application.applicationContext)
    private val repoClientes = ClientesRepository(application.applicationContext)

    private val _state = MutableStateFlow(RevisionLcFormState())
    val state: StateFlow<RevisionLcFormState> = _state

    fun init(clienteId: Long) {
        if (_state.value.clienteId != 0L) return

        val hoyBackend = LocalDate.now().toString()

        viewModelScope.launch {
            try {
                val cliente = repoClientes.obtenerClientePorId(clienteId.toInt())

                _state.value = _state.value.copy(
                    clienteId = clienteId,
                    nombre = cliente.nombre,
                    apellidos = cliente.apellidos,
                    fechaRevision = hoyBackend,
                    error = null
                )
            } catch (_: Exception) {
                _state.value = _state.value.copy(
                    clienteId = clienteId,
                    nombre = "",
                    apellidos = "",
                    fechaRevision = hoyBackend,
                    error = "No se pudo cargar la ficha del cliente."
                )
            }
        }
    }

    fun updateFechaRevision(v: String) = _state.update { it.copy(fechaRevision = v) }
    fun updateAnamnesis(v: String) = _state.update { it.copy(anamnesis = v) }
    fun updateOtrasPruebas(v: String) = _state.update { it.copy(otrasPruebas = v) }

    fun updateEsferaOd(v: Double?) = _state.update { it.copy(esferaOd = v) }
    fun updateCilindroOd(v: Double?) = _state.update { it.copy(cilindroOd = v) }
    fun updateEjeOd(v: Int?) = _state.update { it.copy(ejeOd = v) }
    fun updateAvOd(v: Double?) = _state.update { it.copy(avOd = v) }
    fun updateAddOd(v: Double?) = _state.update { it.copy(addOd = v) }

    fun updateDominanteOd(value: Boolean) {
        _state.value = if (value) {
            _state.value.copy(
                dominanteOd = true,
                dominanteOi = false
            )
        } else {
            _state.value.copy(
                dominanteOd = false,
                dominanteOi = true
            )
        }
    }

    fun updateTipoLenteOd(v: String) = _state.update { it.copy(tipoLenteOd = v) }

    fun updateEsferaOi(v: Double?) = _state.update { it.copy(esferaOi = v) }
    fun updateCilindroOi(v: Double?) = _state.update { it.copy(cilindroOi = v) }
    fun updateEjeOi(v: Int?) = _state.update { it.copy(ejeOi = v) }
    fun updateAvOi(v: Double?) = _state.update { it.copy(avOi = v) }
    fun updateAddOi(v: Double?) = _state.update { it.copy(addOi = v) }

    fun updateTipoLenteOi(value: String) {
        _state.value = _state.value.copy(tipoLenteOi = value)
    }

    fun updateDominanteOi(value: Boolean) {
        _state.value = if (value) {
            _state.value.copy(
                dominanteOi = true,
                dominanteOd = false
            )
        } else {
            _state.value.copy(
                dominanteOi = false,
                dominanteOd = true
            )
        }
    }

    fun guardar() {
        val s = _state.value
        if (s.isSaving) return

        if (s.fechaRevision.length != 10) {
            _state.update { it.copy(error = "Fecha inválida.") }
            return
        }

        _state.update { it.copy(isSaving = true, error = null, ok = false) }

        viewModelScope.launch {
            try {
                val body = RevisionLcCreateRequestDto(
                    fecha_revision = s.fechaRevision,
                    anamnesis = s.anamnesis.ifBlank { null },
                    otras_pruebas = s.otrasPruebas.ifBlank { null },

                    esfera_od = s.esferaOd,
                    cilindro_od = s.cilindroOd,
                    eje_od = s.ejeOd,
                    av_od = s.avOd,
                    add_od = s.addOd,
                    dominante_od = if (s.dominanteOd) 1 else 0,
                    tipo_lente_od = s.tipoLenteOd.ifBlank { null },

                    esfera_oi = s.esferaOi,
                    cilindro_oi = s.cilindroOi,
                    eje_oi = s.ejeOi,
                    av_oi = s.avOi,
                    add_oi = s.addOi,
                    dominante_oi = if (s.dominanteOi) 1 else 0,
                    tipo_lente_oi = s.tipoLenteOi.ifBlank { null }
                )

                val resp = repo.crearRevisionLc(s.clienteId, body)

                _state.update {
                    it.copy(
                        isSaving = false,
                        ok = true,
                        idRevisionCreada = resp.id_revision_lc
                    )
                }
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = "No se pudo guardar la revisión de lentes de contacto. Inténtalo de nuevo."
                    )
                }
            }
        }
    }

    fun limpiarOk() {
        _state.update { it.copy(ok = false) }
    }
}