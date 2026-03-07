package com.opticeasy.app.ui.screens.revisiones.gafa

data class GraduacionUsadaState(
    val esfera: String = "0.00",
    val cilindro: String = "0.00",
    val eje: String = "0",
    val av: String = "1.0"
)

data class GraduacionNuevaState(
    val esfera: String = "0.00",
    val cilindro: String = "0.00",
    val eje: String = "0",
    val av: String = "1.0",
    val add: String = "0.00",
    val prisma: String = "0.0",
    val ccf: String = "0.00",
    val arn: String = "0.00",
    val arp: String = "0.00",
    val dominante: Boolean = false
)

data class RevisionGafaFormState(
    // Identificación
    val clienteId: Long,

    // Cabecera
    val nombre: String = "",
    val apellidos: String = "",
    val codigoCliente: String = "",

    // Datos generales
    val fechaRevision: String = "", // "YYYY-MM-DD"
    val anamnesis: String = "",
    val otrasPruebas: String = "",

    // GRADUACIÓN USADA
    val usadaOD: GraduacionUsadaState = GraduacionUsadaState(),
    val usadaOI: GraduacionUsadaState = GraduacionUsadaState(),

    // NUEVA GRADUACIÓN
    val nuevaOD: GraduacionNuevaState = GraduacionNuevaState(),
    val nuevaOI: GraduacionNuevaState = GraduacionNuevaState(),

    // UI state
    val loading: Boolean = false,
    val error: String? = null,
    val savedOk: Boolean = false
)

