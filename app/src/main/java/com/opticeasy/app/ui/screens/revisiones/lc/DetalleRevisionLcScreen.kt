package com.opticeasy.app.ui.screens.revisiones.lc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.viewmodel.revisiones.DetalleRevisionLcState
import com.opticeasy.app.viewmodel.revisiones.DetalleRevisionLcViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun fechaEsFromIsoOrSameLc(value: String): String {
    return try {
        val d = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
        d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))
    } catch (_: Throwable) {
        value
    }
}

private fun fmt2Lc(v: Double): String = String.format(Locale.US, "%.2f", v)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun readOnlyFieldColorsLc() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surface,
    errorContainerColor = MaterialTheme.colorScheme.surface,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledBorderColor = MaterialTheme.colorScheme.outline,
    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Composable
private fun ReadOnlyFieldLc(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        minLines = minLines,
        colors = readOnlyFieldColorsLc()
    )
}

@Composable
private fun GraduacionBlockReadOnlyLc(
    esfera: String,
    cilindro: String,
    eje: String,
    av: String,
    add: String,
    dominante: Boolean,
    tipoLente: String
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyFieldLc("Esfera", esfera, Modifier.weight(1f))
        ReadOnlyFieldLc("Cilindro", cilindro, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyFieldLc("Eje", eje, Modifier.weight(1f))
        ReadOnlyFieldLc("AV", av, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyFieldLc("ADD", add, Modifier.weight(1f))
        ReadOnlyFieldLc("Tipo lente", tipoLente, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dominante")
        Switch(
            checked = dominante,
            onCheckedChange = null
        )
    }
}

private fun orDashLc(value: String?): String = value ?: "—"
private fun orDashLc(value: Int?): String = value?.toString() ?: "—"
private fun orDashLc(value: Double?): String = value?.let { fmt2Lc(it) } ?: "—"

@Composable
fun DetalleRevisionLcScreen(
    idRevision: Long,
    onVolver: () -> Unit,
    vm: DetalleRevisionLcViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    val green = MaterialTheme.colorScheme.primary

    LaunchedEffect(idRevision) {
        vm.cargar(idRevision)
    }

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Detalle revisión de lentes de contacto",
                style = MaterialTheme.typography.headlineMedium,
                color = green
            )

            Spacer(Modifier.height(8.dp))

            Divider(color = MaterialTheme.colorScheme.outline)

            Spacer(Modifier.height(12.dp))

            when (val s = state) {

                is DetalleRevisionLcState.Loading,
                is DetalleRevisionLcState.Idle -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator()
                        Text("Cargando detalle...")
                    }
                }

                is DetalleRevisionLcState.Error -> {
                    Text(
                        text = s.message,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onVolver,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Atrás")
                    }
                }

                is DetalleRevisionLcState.Success -> {
                    val r = s.revision
                    val fechaEs = fechaEsFromIsoOrSameLc(r.fecha_revision)

                    Text("Fecha: $fechaEs", color = green)
                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Optometrista: ${orDashLc(r.optometrista)} · Nº colegiado: ${orDashLc(r.optometrista_colegiado)}",
                        color = green
                    )

                    Spacer(Modifier.height(20.dp))

                    ReadOnlyFieldLc(
                        label = "Anamnesis",
                        value = orDashLc(r.anamnesis),
                        minLines = 2
                    )

                    Spacer(Modifier.height(12.dp))

                    ReadOnlyFieldLc(
                        label = "Otras pruebas",
                        value = orDashLc(r.otras_pruebas),
                        minLines = 2
                    )

                    Spacer(Modifier.height(20.dp))

                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(12.dp)) {

                            Text("Graduación", color = green)

                            Spacer(Modifier.height(8.dp))
                            Text("OD", color = green)

                            GraduacionBlockReadOnlyLc(
                                esfera = orDashLc(r.esfera_od),
                                cilindro = orDashLc(r.cilindro_od),
                                eje = orDashLc(r.eje_od),
                                av = orDashLc(r.av_od),
                                add = orDashLc(r.add_od),
                                dominante = r.dominante_od == 1,
                                tipoLente = orDashLc(r.tipo_lente_od)
                            )

                            Spacer(Modifier.height(8.dp))
                            Text("OI", color = green)

                            GraduacionBlockReadOnlyLc(
                                esfera = orDashLc(r.esfera_oi),
                                cilindro = orDashLc(r.cilindro_oi),
                                eje = orDashLc(r.eje_oi),
                                av = orDashLc(r.av_oi),
                                add = orDashLc(r.add_oi),
                                dominante = r.dominante_oi == 1,
                                tipoLente = orDashLc(r.tipo_lente_oi)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onVolver,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Atrás")
                    }
                }
            }
        }
    }
}

