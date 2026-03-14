package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.common.OkResponseDto
import com.opticeasy.app.data.remote.dto.rgpd.FirmaRgpdRequestDto

class RgpdRepository(
    context: Context
) {

    private val api = RetrofitClient.getApi(context)

    suspend fun guardarFirma(clienteId: Int, req: FirmaRgpdRequestDto): OkResponseDto {
        return api.crearFirmaRgpd(clienteId, req)
    }
}
