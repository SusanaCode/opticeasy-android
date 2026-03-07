package com.opticeasy.app.data.repository


import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.auth.RegisterRequestDto
import com.opticeasy.app.data.remote.dto.auth.UsuarioDto
import com.opticeasy.app.data.remote.dto.auth.LoginRequestDto

class AuthRepository {

    private val api = RetrofitClient.api

    suspend fun login(login: String, password: String): UsuarioDto {
        return api.login(LoginRequestDto(login = login, password = password))
    }

    suspend fun register(req: RegisterRequestDto): UsuarioDto {
       return api.register(req)
    }
}



