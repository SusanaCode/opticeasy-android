package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.auth.LoginRequestDto
import com.opticeasy.app.data.remote.dto.auth.LoginResponseDto
import com.opticeasy.app.data.remote.dto.auth.RegisterRequestDto
import com.opticeasy.app.data.remote.dto.auth.UsuarioDto

class AuthRepository(
    context: Context
) {

    private val api = RetrofitClient.getApi(context)

    suspend fun login(login: String, password: String): LoginResponseDto {
        return api.login(LoginRequestDto(login = login, password = password))
    }

    suspend fun register(req: RegisterRequestDto): UsuarioDto {
        return api.register(req)
    }
}



