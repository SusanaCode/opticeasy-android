package com.opticeasy.app.data.remote.dto.revisiones_gafa

import com.google.gson.annotations.SerializedName

data class RevisionGafaCreateResponseDto(
    val ok: Boolean,
    @SerializedName("id_revision_gafa") val idRevisionGafa: Long
)