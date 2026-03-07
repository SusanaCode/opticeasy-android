package com.opticeasy.app.data.remote.dto.auth

data class RegisterRequestDto(
    val nombre_usuario: String,
    val apellidos_usuario: String,
    val nick_usuario: String,
    val email: String,
    val numero_colegiado: Int?,    // null si comercial
    val password: String,
    val codigo_centro: String,
    val rol: String                // "optico" o "comercial"
)