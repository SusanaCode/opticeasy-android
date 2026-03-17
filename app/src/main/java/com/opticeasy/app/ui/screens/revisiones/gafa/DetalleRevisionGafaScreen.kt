package com.opticeasy.app.ui.screens.revisiones.gafa

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
import com.opticeasy.app.viewmodel.revisiones.DetalleRevisionGafaState
import com.opticeasy.app.viewmodel.revisiones.DetalleRevisionGafaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun fechaEsFromIsoOrSame(value: String): String {
    return try {
        val d = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
        d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))
    } catch (_: Throwable) {
        value
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun readOnlyFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surface,
    errorContainerColor = MaterialTheme.colorScheme.surface,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledBorderColor = MaterialTheme.colorScheme.outline,
    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Composable
private fun ReadOnlyField(
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
        colors = readOnlyFieldColors()
    )
}

@Composable
private fun GraduacionUsadaBlockReadOnly(
    esfera: String,
    cilindro: String,
    eje: String,
    av: String
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("Esfera", esfera, Modifier.weight(1f))
        ReadOnlyField("Cilindro", cilindro, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("Eje", eje, Modifier.weight(1f))
        ReadOnlyField("AV", av, Modifier.weight(1f))
    }
}

@Composable
private fun GraduacionNuevaBlockReadOnly(
    esfera: String,
    cilindro: String,
    eje: String,
    av: String,
    add: String,
    prisma: String,
    ccf: String,
    arn: String,
    arp: String,
    dominante: Boolean
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("Esfera", esfera, Modifier.weight(1f))
        ReadOnlyField("Cilindro", cilindro, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("Eje", eje, Modifier.weight(1f))
        ReadOnlyField("AV", av, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("ADD", add, Modifier.weight(1f))
        ReadOnlyField("Prisma", prisma, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("CCF", ccf, Modifier.weight(1f))
        ReadOnlyField("ARN", arn, Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReadOnlyField("ARP", arp, Modifier.weight(1f))
        Spacer(Modifier.weight(1f))
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dominante")
        Switch(
            checked = dominante,
            onCheckedChange = null   // 👈 CLAVE AQUÍ
        )
    }
}

private fun orDash(value: String?): String = value ?: "—"
private fun orDash(value: Int?): String = value?.toString() ?: "—"

@Composable
fun DetalleRevisionGafaScreen(
    idRevision: Long,
    onVolver: () -> Unit,
    vm: DetalleRevisionGafaViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    val green = MaterialTheme.colorScheme.primary
    val shape16 = RoundedCornerShape(16.dp)

    LaunchedEffect(idRevision) {
        vm.cargar(idRevision)
    }

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Detalle revisión de gafa",
                style = MaterialTheme.typography.headlineMedium,
                color = green
            )

            Spacer(Modifier.height(8.dp))

            Divider(color = MaterialTheme.colorScheme.outline)

            Spacer(Modifier.height(12.dp))

            when (val s = state) {

                is DetalleRevisionGafaState.Loading,
                is DetalleRevisionGafaState.Idle -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CircularProgressIndicator()
                        Text("Cargando detalle...")
                    }
                }

                is DetalleRevisionGafaState.Error -> {
                    Text(s.message, color = MaterialTheme.colorScheme.error)

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onVolver,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Atrás")
                    }
                }

                is DetalleRevisionGafaState.Success -> {
                    val r = s.revision
                    val fechaEs = fechaEsFromIsoOrSame(r.fecha_revision)

                    Text("Fecha: $fechaEs", color = green)
                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Optometrista: ${orDash(r.optometrista)} · Nº colegiado: ${orDash(r.optometrista_colegiado)}",
                        color = green
                    )

                    Spacer(Modifier.height(20.dp))

                    ReadOnlyField("Anamnesis", orDash(r.anamnesis), minLines = 3)

                    Spacer(Modifier.height(12.dp))

                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(12.dp)) {

                            Text("Graduación usada", color = green)

                            Spacer(Modifier.height(8.dp))
                            Text("OD", color = green)

                            GraduacionUsadaBlockReadOnly(
                                orDash(r.esfera_usada_od),
                                orDash(r.cilindro_usada_od),
                                orDash(r.eje_usada_od),
                                orDash(r.av_usada_od)
                            )

                            Spacer(Modifier.height(8.dp))
                            Text("OI", color = green)

                            GraduacionUsadaBlockReadOnly(
                                orDash(r.esfera_usada_oi),
                                orDash(r.cilindro_usada_oi),
                                orDash(r.eje_usada_oi),
                                orDash(r.av_usada_oi)
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(12.dp)) {

                            Text("Nueva graduación", color = green)

                            Spacer(Modifier.height(8.dp))
                            Text("OD", color = green)

                            GraduacionNuevaBlockReadOnly(
                                orDash(r.esfera_od),
                                orDash(r.cilindro_od),
                                orDash(r.eje_od),
                                orDash(r.av_od),
                                orDash(r.add_od),
                                orDash(r.prisma_od),
                                orDash(r.ccf_od),
                                orDash(r.arn_od),
                                orDash(r.arp_od),
                                r.dominante_od == 1
                            )

                            Spacer(Modifier.height(8.dp))
                            Text("OI", color = green)

                            GraduacionNuevaBlockReadOnly(
                                orDash(r.esfera_oi),
                                orDash(r.cilindro_oi),
                                orDash(r.eje_oi),
                                orDash(r.av_oi),
                                orDash(r.add_oi),
                                orDash(r.prisma_oi),
                                orDash(r.ccf_oi),
                                orDash(r.arn_oi),
                                orDash(r.arp_oi),
                                r.dominante_oi == 1
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    ReadOnlyField("Otras pruebas", orDash(r.otras_pruebas), minLines = 3)

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

