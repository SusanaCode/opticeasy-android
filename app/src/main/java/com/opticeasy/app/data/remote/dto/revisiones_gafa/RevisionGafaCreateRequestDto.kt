package com.opticeasy.app.data.remote.dto.revisiones_gafa

import com.google.gson.annotations.SerializedName

data class RevisionGafaCreateRequestDto(
    // obligatorio en backend (viene de sesión)
    @SerializedName("id_optometrista") val idOptometrista: Long,

    // opcional: si no mandas, backend pone hoy
    @SerializedName("fecha_revision") val fechaRevision: String? = null,

    val anamnesis: String? = null,
    @SerializedName("otras_pruebas") val otrasPruebas: String? = null,

    // --- USADA OD
    @SerializedName("esfera_usada_od") val esferaUsadaOd: Double? = null,
    @SerializedName("cilindro_usada_od") val cilindroUsadaOd: Double? = null,
    @SerializedName("eje_usada_od") val ejeUsadaOd: Int? = null,
    @SerializedName("av_usada_od") val avUsadaOd: Double? = null,

    // --- RESULTANTE OD
    @SerializedName("esfera_od") val esferaOd: Double? = null,
    @SerializedName("cilindro_od") val cilindroOd: Double? = null,
    @SerializedName("eje_od") val ejeOd: Int? = null,
    @SerializedName("av_od") val avOd: Double? = null,
    @SerializedName("add_od") val addOd: Double? = null,
    @SerializedName("prisma_od") val prismaOd: Double? = null,
    @SerializedName("ccf_od") val ccfOd: Double? = null,
    @SerializedName("arn_od") val arnOd: Double? = null,
    @SerializedName("arp_od") val arpOd: Double? = null,
    @SerializedName("dominante_od") val dominanteOd: Int = 0, // 0/1

    // --- USADA OI
    @SerializedName("esfera_usada_oi") val esferaUsadaOi: Double? = null,
    @SerializedName("cilindro_usada_oi") val cilindroUsadaOi: Double? = null,
    @SerializedName("eje_usada_oi") val ejeUsadaOi: Int? = null,
    @SerializedName("av_usada_oi") val avUsadaOi: Double? = null,

    // --- RESULTANTE OI
    @SerializedName("esfera_oi") val esferaOi: Double? = null,
    @SerializedName("cilindro_oi") val cilindroOi: Double? = null,
    @SerializedName("eje_oi") val ejeOi: Int? = null,
    @SerializedName("av_oi") val avOi: Double? = null,
    @SerializedName("add_oi") val addOi: Double? = null,
    @SerializedName("prisma_oi") val prismaOi: Double? = null,
    @SerializedName("ccf_oi") val ccfOi: Double? = null,
    @SerializedName("arn_oi") val arnOi: Double? = null,
    @SerializedName("arp_oi") val arpOi: Double? = null,
    @SerializedName("dominante_oi") val dominanteOi: Int = 0 // 0/1
)