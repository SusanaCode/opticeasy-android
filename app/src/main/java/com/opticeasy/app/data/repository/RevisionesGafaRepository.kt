package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.local.SessionManager
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateRequestDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateResponseDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaListItemDto
import kotlinx.coroutines.flow.first

class RevisionesGafaRepository(
    context: Context
) {
    private val api = RetrofitClient.api
    private val sessionManager = SessionManager(context)

    suspend fun crearRevisionGafa(
        clienteId: Long,
        request: RevisionGafaCreateRequestDto
    ): RevisionGafaCreateResponseDto {

        val idOptometrista = sessionManager.idUsuario.first()
            ?: throw IllegalStateException("No hay sesión iniciada (id_usuario null)")

        // 🔒 Fuerza id_optometrista desde sesión (no depende de la UI)
        val finalRequest = request.copy(idOptometrista = idOptometrista)

        return api.crearRevisionGafa(clienteId, finalRequest)
    }

    suspend fun listarRevisionesGafa(clienteId: Long): List<RevisionGafaListItemDto> {
        return api.listarRevisionesGafa(clienteId)
    }
}

