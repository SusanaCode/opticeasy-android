package com.opticeasy.app.data.remote.dto.Revisiones_lc

data class RevisionLcCreateResponseDto(
    val id_revision_lc: Long,
    val id_cliente: Long,
    val id_optometrista: Long,
    val fecha_revision: String,

    val anamnesis: String? = null,
    val otras_pruebas: String? = null,

    // OD
    val esfera_od: Double? = null,
    val cilindro_od: Double? = null,
    val eje_od: Int? = null,
    val av_od: Double? = null,
    val add_od: Double? = null,
    val dominante_od: Int? = null,
    val tipo_lente_od: String? = null,

    // OI
    val esfera_oi: Double? = null,
    val cilindro_oi: Double? = null,
    val eje_oi: Int? = null,
    val av_oi: Double? = null,
    val add_oi: Double? = null,
    val dominante_oi: Int? = null,
    val tipo_lente_oi: String? = null,

    // Extra viene del SELECT con usuarios (puede venir null)
    val optometrista: String? = null,
    val optometrista_colegiado: String? = null
)