package com.opticeasy.app.data.remote.dto.revisiones_gafa

data class RevisionGafaListItemDto(
    val id_revision_gafa: Int,
    val id_cliente: Int,
    val fecha_revision: String,

    val esfera_od: String?,
    val cilindro_od: String?,
    val eje_od: Int?,
    val av_od: String?,
    val add_od: String?,



    val esfera_oi: String?,
    val cilindro_oi: String?,
    val eje_oi: Int?,
    val av_oi: String?,
    val add_oi: String?,


    val id_optometrista: Int?,
    val optometrista: String?,
    val optometrista_colegiado: Int?
)