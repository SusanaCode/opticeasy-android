package com.opticeasy.app.data.remote.dto.Revisiones_lc

data class RevisionLcDetalleDto(
    val id_revision_lc: Int,
    val id_cliente: Int,
    val id_optometrista: Int?,
    val fecha_revision: String,

    val anamnesis: String?,
    val otras_pruebas: String?,

    val esfera_od: Double?,
    val cilindro_od: Double?,
    val eje_od: Int?,
    val av_od: Double?,
    val add_od: Double?,
    val dominante_od: Int?,
    val tipo_lente_od: String?,

    val esfera_oi: Double?,
    val cilindro_oi: Double?,
    val eje_oi: Int?,
    val av_oi: Double?,
    val add_oi: Double?,
    val dominante_oi: Int?,
    val tipo_lente_oi: String?,

    val optometrista: String?,
    val optometrista_colegiado: Int?
)

