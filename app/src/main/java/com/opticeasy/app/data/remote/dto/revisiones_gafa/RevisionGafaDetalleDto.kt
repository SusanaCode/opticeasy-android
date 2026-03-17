package com.opticeasy.app.data.remote.dto.revisiones_gafa

data class RevisionGafaDetalleDto(
    val id_revision_gafa: Int,
    val id_cliente: Int,
    val id_optometrista: Int?,
    val fecha_revision: String,

    val anamnesis: String?,
    val otras_pruebas: String?,

    // Usada OD
    val esfera_usada_od: String?,
    val cilindro_usada_od: String?,
    val eje_usada_od: Int?,
    val av_usada_od: String?,

    // Resultante OD
    val esfera_od: String?,
    val cilindro_od: String?,
    val eje_od: Int?,
    val av_od: String?,
    val add_od: String?,
    val prisma_od: String?,
    val ccf_od: String?,
    val arn_od: String?,
    val arp_od: String?,
    val dominante_od: Int?,

    // Usada OI
    val esfera_usada_oi: String?,
    val cilindro_usada_oi: String?,
    val eje_usada_oi: Int?,
    val av_usada_oi: String?,

    // Resultante OI
    val esfera_oi: String?,
    val cilindro_oi: String?,
    val eje_oi: Int?,
    val av_oi: String?,
    val add_oi: String?,
    val prisma_oi: String?,
    val ccf_oi: String?,
    val arn_oi: String?,
    val arp_oi: String?,
    val dominante_oi: Int?,

    // Optometrista
    val optometrista: String?,
    val optometrista_colegiado: Int?
)

