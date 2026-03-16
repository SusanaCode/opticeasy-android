package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateRequestDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateResponseDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaListItemDto

class RevisionesGafaRepository(
    context: Context
) {
    private val api = RetrofitClient.getApi(context)

    suspend fun crearRevisionGafa(
        clienteId: Long,
        request: RevisionGafaCreateRequestDto
    ): RevisionGafaCreateResponseDto {
        return api.crearRevisionGafa(clienteId, request)
    }

    suspend fun listarRevisionesGafa(clienteId: Long): List<RevisionGafaListItemDto> {
        return api.listarRevisionesGafa(clienteId)
    }
}
