package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.local.SessionManager
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateRequestDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateResponseDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcListItemDto
import kotlinx.coroutines.flow.first

class RevisionesLcRepository(
    context: Context
) {
    private val api = RetrofitClient.getApi(context)
    private val sessionManager = SessionManager(context)

    suspend fun crearRevisionLc(
        clienteId: Long,
        request: RevisionLcCreateRequestDto
    ): RevisionLcCreateResponseDto {

        val idUsuario = sessionManager.idUsuario.first() ?: 0L

        val bodyFinal = request.copy(
            id_optometrista = idUsuario
        )

        return api.crearRevisionLc(clienteId, bodyFinal)
    }

    suspend fun listarRevisionesLc(clienteId: Long): List<RevisionLcListItemDto> {
        return api.listarRevisionesLc(clienteId)
    }
}

