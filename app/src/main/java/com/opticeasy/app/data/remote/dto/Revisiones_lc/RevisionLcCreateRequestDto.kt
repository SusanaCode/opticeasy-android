package com.opticeasy.app.data.remote.dto.Revisiones_lc

data class RevisionLcCreateRequestDto(
    val id_optometrista: Long,

    val fecha_revision: String, // "YYYY-MM-DD"

    val anamnesis: String? = null,
    val otras_pruebas: String? = null,

    // OD
    val esfera_od: Double? = null,
    val cilindro_od: Double? = null,
    val eje_od: Int? = null,
    val av_od: Double? = null,
    val add_od: Double? = null,
    val dominante_od: Int? = 0,      // backend usa 0/1
    val tipo_lente_od: String? = null,

    // OI
    val esfera_oi: Double? = null,
    val cilindro_oi: Double? = null,
    val eje_oi: Int? = null,
    val av_oi: Double? = null,
    val add_oi: Double? = null,
    val dominante_oi: Int? = 0,      // backend usa 0/1
    val tipo_lente_oi: String? = null
)