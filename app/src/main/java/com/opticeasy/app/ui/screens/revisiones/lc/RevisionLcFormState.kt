package com.opticeasy.app.ui.screens.revisiones.lc

data class RevisionLcFormState(
    // datos del cliente (para cabecera)
    val clienteId: Long = 0L,
    val nombre: String = "",
    val apellidos: String = "",


    // campos generales
    val fechaRevision: String = "", // YYYY-MM-DD
    val anamnesis: String = "",
    val otrasPruebas: String = "",

    // OD
    val esferaOd: Double? = 0.0,
    val cilindroOd: Double? = null,
    val ejeOd: Int? = null,
    val avOd: Double? = null,
    val addOd: Double? = null,
    val dominanteOd: Boolean = false,
    val tipoLenteOd: String = "",

    // OI
    val esferaOi: Double? = null,
    val cilindroOi: Double? = null,
    val ejeOi: Int? = null,
    val avOi: Double? = null,
    val addOi: Double? = null,
    val dominanteOi: Boolean = false,
    val tipoLenteOi: String = "",

    // UI state
    val isSaving: Boolean = false,
    val ok: Boolean = false,
    val idRevisionCreada: Long? = null,
    val error: String? = null
)