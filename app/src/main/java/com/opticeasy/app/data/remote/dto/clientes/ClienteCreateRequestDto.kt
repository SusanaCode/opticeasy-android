package com.opticeasy.app.data.remote.dto.clientes

data class ClienteCreateRequestDto(
    val nombre: String,
    val apellidos: String,
    val dni: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val cp: String? = null,
    val poblacion: String? = null,
    val provincia: String? = null,
    val fecha_nacimiento: String? = null,
    val correo_electronico: String? = null,
    val firma_rgpd: Int = 0,
    val activo: Int = 1
)