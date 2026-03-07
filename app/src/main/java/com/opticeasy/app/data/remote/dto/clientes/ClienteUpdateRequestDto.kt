package com.opticeasy.app.data.remote.dto.clientes

import com.google.gson.annotations.SerializedName

data class ClienteUpdateRequestDto(
    val nombre: String,
    val apellidos: String,
    val telefono: String?,
    val direccion: String?,
    @SerializedName("cp") val cp: String?,
    val poblacion: String?,
    val provincia: String?,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String?,
    val dni: String,
    @SerializedName("correo_electronico") val correoElectronico: String?,
    @SerializedName("firma_rgpd") val firmaRgpd: Int,
    val notas: String?,
    val activo: Int
)