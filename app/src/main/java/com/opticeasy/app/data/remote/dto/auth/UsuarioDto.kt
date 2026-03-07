package com.opticeasy.app.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class UsuarioDto(
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("apellidos_usuario") val apellidosUsuario: String,
    @SerializedName("nick_usuario") val nickUsuario: String,
    val email: String,
    @SerializedName("numero_colegiado") val numeroColegiado: Int?,
    @SerializedName("codigo_centro") val codigoCentro: String,
    val rol: String,
    val activo: Int
)