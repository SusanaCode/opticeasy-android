package com.opticeasy.app.data.repository

import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.rgpd.FirmaRgpdRequestDto
import com.opticeasy.app.data.remote.dto.common.OkResponseDto

class RgpdRepository {
    suspend fun guardarFirma(clienteId: Int, req: FirmaRgpdRequestDto): OkResponseDto {
        return RetrofitClient.api.crearFirmaRgpd(clienteId, req)
    }
}
