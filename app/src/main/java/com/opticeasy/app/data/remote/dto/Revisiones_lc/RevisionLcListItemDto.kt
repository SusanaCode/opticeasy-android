package com.opticeasy.app.data.remote.dto.Revisiones_lc

data class RevisionLcListItemDto(
    val id_revision_lc: Int,
    val id_cliente: Int,
    val fecha_revision: String,

    val esfera_od: String?,
    val cilindro_od: String?,
    val eje_od: Int?,
    val av_od: String?,
    val add_od: String?,
    val dominante_od: String?,
    val tipo_lente_od: String?,

    val esfera_oi: String?,
    val cilindro_oi: String?,
    val eje_oi: Int?,
    val av_oi: String?,
    val add_oi: String?,
    val dominante_oi: String?,
    val tipo_lente_oi: String?,

    val id_optometrista: Int?,
    val optometrista: String?,
    val optometrista_colegiado: Int?
)