package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateRequestDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateResponseDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcListItemDto

class RevisionesLcRepository(
    context: Context
) {
    private val api = RetrofitClient.getApi(context)

    suspend fun crearRevisionLc(
        clienteId: Long,
        request: RevisionLcCreateRequestDto
    ): RevisionLcCreateResponseDto {
        return api.crearRevisionLc(clienteId, request)
    }

    suspend fun listarRevisionesLc(clienteId: Long): List<RevisionLcListItemDto> {
        return api.listarRevisionesLc(clienteId)
    }
}

