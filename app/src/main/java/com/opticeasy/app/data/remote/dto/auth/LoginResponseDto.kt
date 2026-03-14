package com.opticeasy.app.data.remote.dto.auth



data class LoginResponseDto(
    val token: String,
    val user: UsuarioDto
)