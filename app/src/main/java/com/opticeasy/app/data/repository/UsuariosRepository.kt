package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.auth.UsuarioDto
import com.opticeasy.app.data.remote.dto.common.OkResponseDto
import com.opticeasy.app.data.remote.dto.usuarios.CambiarActivoRequestDto
import com.opticeasy.app.data.remote.dto.usuarios.CambiarPasswordRequestDto

class UsuariosRepository(private val context: Context) {

    suspend fun listarUsuarios(): List<UsuarioDto> {
        return RetrofitClient.getApi(context).listarUsuarios()
    }

    suspend fun cambiarPasswordUsuario(
        idUsuario: Int,
        nuevaPassword: String
    ): OkResponseDto {
        return RetrofitClient.getApi(context).cambiarPasswordUsuario(
            id = idUsuario,
            body = CambiarPasswordRequestDto(password = nuevaPassword)
        )
    }

    suspend fun cambiarActivoUsuario(
        idUsuario: Int,
        activo: Int
    ): UsuarioDto {
        return RetrofitClient.getApi(context).cambiarActivoUsuario(
            id = idUsuario,
            body = CambiarActivoRequestDto(activo = activo)
        )
    }
}

