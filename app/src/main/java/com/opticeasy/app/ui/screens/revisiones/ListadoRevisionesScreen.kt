package com.opticeasy.app.ui.screens.revisiones

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcListItemDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaListItemDto
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.screens.revisiones.pdf.PdfRevisionesUtils
import com.opticeasy.app.ui.theme.OpticCardBg
import com.opticeasy.app.viewmodel.revisiones.RevisionesListadoState
import com.opticeasy.app.viewmodel.revisiones.RevisionesListadoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ListadoRevisionesScreen(
    clienteId: Long,
    onBack: () -> Unit,
    onIrCliente: () -> Unit,
    onAbrirPdf: (String, String) -> Unit
) {
    val vm: RevisionesListadoViewModel = viewModel()
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val green = MaterialTheme.colorScheme.primary
    val shape16 = RoundedCornerShape(16.dp)

    LaunchedEffect(clienteId) {
        vm.cargar(clienteId)
    }

    BaseScreen(contentTopPadding = 0.dp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Listado de revisiones",
                style = MaterialTheme.typography.headlineMedium,
                color = green,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Cliente $clienteId",
                style = MaterialTheme.typography.bodyMedium,
                color = green,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(50.dp))

            when (val s = state) {
                is RevisionesListadoState.Loading -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator()
                        Spacer(Modifier.width(12.dp))
                        Text("Cargando revisiones...")
                    }
                }

                is RevisionesListadoState.Error -> {
                    Text(
                        text = "No se pudieron cargar las revisiones.",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = onBack) {
                        Text("Volver al cliente")
                    }
                }

                is RevisionesListadoState.Success -> {
                    SeccionGafa(
                        items = s.revisionesGafa,
                        context = context,
                        clienteId = clienteId,
                        onAbrirPdf = onAbrirPdf
                    )

                    Spacer(Modifier.height(18.dp))

                    SeccionLc(
                        items = s.revisionesLc,
                        context = context,
                        clienteId = clienteId,
                        onAbrirPdf = onAbrirPdf
                    )

                    Spacer(Modifier.height(18.dp))

                    Button(
                        onClick = onIrCliente,
                        shape = shape16,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Atrás")
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun SeccionGafa(
    items: List<RevisionGafaListItemDto>,
    context: Context,
    clienteId: Long,
    onAbrirPdf: (String, String) -> Unit
) {
    Text("REVISIONES PARA GAFA", style = MaterialTheme.typography.titleSmall)
    Divider(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp)
    )

    if (items.isEmpty()) {
        Text("No hay revisiones de gafa")
        return
    }

    items.forEach { item ->
        RevisionCard(
            fecha = item.fecha_revision,
            od = "ESF ${item.esfera_od ?: "—"}  CIL ${item.cilindro_od ?: "—"}  EJE ${item.eje_od?.toString() ?: "—"}  AV ${item.av_od ?: "—"}  ADD ${item.add_od ?: "—"}",
            oi = "ESF ${item.esfera_oi ?: "—"}  CIL ${item.cilindro_oi ?: "—"}  EJE ${item.eje_oi?.toString() ?: "—"}  AV ${item.av_oi ?: "—"}  ADD ${item.add_oi ?: "—"}",
            optometrista = item.optometrista,
            onClick = {
                val file = PdfRevisionesUtils.generarPdfRevisionGafa(
                    context = context,
                    nombre = "",
                    apellidos = "",
                    codigoCliente = clienteId.toString(),
                    fechaRevisionYYYYMMDD = item.fecha_revision,
                    od = PdfRevisionesUtils.RevisionOjoPdf(
                        esfera = item.esfera_od,
                        cilindro = item.cilindro_od,
                        eje = item.eje_od?.toString(),
                        av = item.av_od,
                        add = item.add_od
                    ),
                    oi = PdfRevisionesUtils.RevisionOjoPdf(
                        esfera = item.esfera_oi,
                        cilindro = item.cilindro_oi,
                        eje = item.eje_oi?.toString(),
                        av = item.av_oi,
                        add = item.add_oi
                    ),
                    optometrista = item.optometrista,
                    numeroColegiado = item.optometrista_colegiado
                )

                onAbrirPdf(file.absolutePath, "Informe de revisión - Gafa")
            }
        )
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun SeccionLc(
    items: List<RevisionLcListItemDto>,
    context: Context,
    clienteId: Long,
    onAbrirPdf: (String, String) -> Unit
) {
    Text("REVISIONES PARA LC", style = MaterialTheme.typography.titleSmall)
    Divider(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 12.dp)
    )

    if (items.isEmpty()) {
        Text("No hay revisiones de LC")
        return
    }

    items.forEach { item ->
        RevisionCard(
            fecha = item.fecha_revision,
            od = "ESF ${item.esfera_od ?: "—"}  CIL ${item.cilindro_od ?: "—"}  EJE ${item.eje_od?.toString() ?: "—"}  AV ${item.av_od ?: "—"}  ADD ${item.add_od ?: "—"}",
            oi = "ESF ${item.esfera_oi ?: "—"}  CIL ${item.cilindro_oi ?: "—"}  EJE ${item.eje_oi?.toString() ?: "—"}  AV ${item.av_oi ?: "—"}  ADD ${item.add_oi ?: "—"}",
            optometrista = item.optometrista,
            onClick = {
                val file = PdfRevisionesUtils.generarPdfRevisionLc(
                    context = context,
                    nombre = "",
                    apellidos = "",
                    codigoCliente = clienteId.toString(),
                    fechaRevisionYYYYMMDD = item.fecha_revision,
                    od = PdfRevisionesUtils.RevisionOjoPdf(
                        esfera = item.esfera_od,
                        cilindro = item.cilindro_od,
                        eje = item.eje_od?.toString(),
                        av = item.av_od,
                        add = item.add_od
                    ),
                    oi = PdfRevisionesUtils.RevisionOjoPdf(
                        esfera = item.esfera_oi,
                        cilindro = item.cilindro_oi,
                        eje = item.eje_oi?.toString(),
                        av = item.av_oi,
                        add = item.add_oi
                    ),
                    optometrista = item.optometrista,
                    numeroColegiado = item.optometrista_colegiado
                )

                onAbrirPdf(file.absolutePath, "Informe de revisión - Lentes de contacto")
            }
        )
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun RevisionCard(
    fecha: String,
    od: String,
    oi: String,
    optometrista: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = OpticCardBg
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AssistChip(
                onClick = onClick,
                label = { Text(formatFecha(fecha)) }
            )

            Column(modifier = Modifier.weight(1f)) {
                Text("GRADUACIÓN OD", style = MaterialTheme.typography.labelSmall)
                Text(od, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(6.dp))
                Text("GRADUACIÓN OI", style = MaterialTheme.typography.labelSmall)
                Text(oi, style = MaterialTheme.typography.bodySmall)
            }

            Surface(
                tonalElevation = 1.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.widthIn(min = 120.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("OPTOMETRISTA", style = MaterialTheme.typography.labelSmall)
                    Text(optometrista ?: "—", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun formatFecha(yyyyMMdd: String): String {
    return try {
        val d = LocalDate.parse(yyyyMMdd, DateTimeFormatter.ISO_LOCAL_DATE)
        d.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    } catch (_: Exception) {
        yyyyMMdd
    }
}
