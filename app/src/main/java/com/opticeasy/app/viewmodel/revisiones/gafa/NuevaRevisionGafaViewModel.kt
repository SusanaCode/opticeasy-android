package com.opticeasy.app.viewmodel.revisiones.gafa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateRequestDto
import com.opticeasy.app.data.repository.RevisionesGafaRepository
import com.opticeasy.app.ui.screens.revisiones.gafa.RevisionGafaFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class NuevaRevisionGafaViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RevisionesGafaRepository(application.applicationContext)

    private val _state = MutableStateFlow(
        RevisionGafaFormState(
            clienteId = 0L,
            fechaRevision = LocalDate.now().toString()
        )
    )
    val state: StateFlow<RevisionGafaFormState> = _state

    fun init(clienteId: Long, nombre: String, apellidos: String, codigoCliente: String) {
        if (_state.value.clienteId != 0L) return
        _state.value = _state.value.copy(
            clienteId = clienteId,
            nombre = nombre,
            apellidos = apellidos,
            codigoCliente = codigoCliente,
            fechaRevision = LocalDate.now().toString(),
            error = null,
            savedOk = false
        )
    }

    fun update(reducer: (RevisionGafaFormState) -> RevisionGafaFormState) {
        _state.value = reducer(_state.value).copy(error = null, savedOk = false)
    }

    fun guardar() {
        val s = _state.value

        if (s.clienteId <= 0L) {
            _state.value = s.copy(error = "clienteId inválido")
            return
        }
        if (s.fechaRevision.isNotBlank() && s.fechaRevision.length != 10) {
            _state.value = s.copy(error = "fecha inválida (YYYY-MM-DD)")
            return
        }

        _state.value = s.copy(loading = true, error = null, savedOk = false)

        viewModelScope.launch {
            try {
                // El backend toma id_optometrista desde el token
                repo.crearRevisionGafa(
                    clienteId = s.clienteId,
                    request = s.toCreateRequestDto()
                )

                _state.value = _state.value.copy(loading = false, savedOk = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Error guardando revisión"
                )
            }
        }
    }
}

private fun RevisionGafaFormState.toCreateRequestDto(): RevisionGafaCreateRequestDto {
    fun String.toD(): Double? = trim().replace(",", ".").toDoubleOrNull()
    fun String.toI(): Int? = trim().toIntOrNull()
    fun String.toNullable(): String? = trim().ifBlank { null }

    return RevisionGafaCreateRequestDto(
        fechaRevision = fechaRevision.toNullable(),
        anamnesis = anamnesis.toNullable(),
        otrasPruebas = otrasPruebas.toNullable(),

        // USADA OD
        esferaUsadaOd = usadaOD.esfera.toD(),
        cilindroUsadaOd = usadaOD.cilindro.toD(),
        ejeUsadaOd = usadaOD.eje.toI(),
        avUsadaOd = usadaOD.av.toD(),

        // NUEVA OD
        esferaOd = nuevaOD.esfera.toD(),
        cilindroOd = nuevaOD.cilindro.toD(),
        ejeOd = nuevaOD.eje.toI(),
        avOd = nuevaOD.av.toD(),
        addOd = nuevaOD.add.toD(),
        prismaOd = nuevaOD.prisma.toD(),
        ccfOd = nuevaOD.ccf.toD(),
        arnOd = nuevaOD.arn.toD(),
        arpOd = nuevaOD.arp.toD(),
        dominanteOd = if (nuevaOD.dominante) 1 else 0,

        // USADA OI
        esferaUsadaOi = usadaOI.esfera.toD(),
        cilindroUsadaOi = usadaOI.cilindro.toD(),
        ejeUsadaOi = usadaOI.eje.toI(),
        avUsadaOi = usadaOI.av.toD(),

        // NUEVA OI
        esferaOi = nuevaOI.esfera.toD(),
        cilindroOi = nuevaOI.cilindro.toD(),
        ejeOi = nuevaOI.eje.toI(),
        avOi = nuevaOI.av.toD(),
        addOi = nuevaOI.add.toD(),
        prismaOi = nuevaOI.prisma.toD(),
        ccfOi = nuevaOI.ccf.toD(),
        arnOi = nuevaOI.arn.toD(),
        arpOi = nuevaOI.arp.toD(),
        dominanteOi = if (nuevaOI.dominante) 1 else 0
    )
}
