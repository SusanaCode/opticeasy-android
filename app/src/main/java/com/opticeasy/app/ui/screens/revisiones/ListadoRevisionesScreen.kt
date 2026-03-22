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
    onAbrirPdf: (String, String) -> Unit,
    onAbrirDetalleGafa: (Long) -> Unit,
    onAbrirDetalleLc: (Long) -> Unit
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
                    val cliente = s.cliente

                    Text(
                        text = "${cliente.nombre} ${cliente.apellidos}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = green,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    SeccionGafa(
                        items = s.revisionesGafa,
                        context = context,
                        clienteId = clienteId,
                        nombre = cliente.nombre,
                        apellidos = cliente.apellidos,
                        onAbrirPdf = onAbrirPdf,
                        onAbrirDetalleGafa = onAbrirDetalleGafa
                    )

                    Spacer(Modifier.height(18.dp))

                    SeccionLc(
                        items = s.revisionesLc,
                        context = context,
                        clienteId = clienteId,
                        nombre = cliente.nombre,
                        apellidos = cliente.apellidos,
                        onAbrirPdf = onAbrirPdf,
                        onAbrirDetalleLc = onAbrirDetalleLc
                    )

                    Spacer(Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = onIrCliente,
                        shape = shape16,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Atrás")
                    }

                    Spacer(Modifier.height(24.dp))
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
    nombre: String,
    apellidos: String,
    onAbrirPdf: (String, String) -> Unit,
    onAbrirDetalleGafa: (Long) -> Unit
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
            onClickPdf = {
                val file = PdfRevisionesUtils.generarPdfRevisionGafa(
                    context = context,
                    nombre = nombre,
                    apellidos = apellidos,
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
            },
            onClickDetalle = {
                onAbrirDetalleGafa(item.id_revision_gafa.toLong())
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
    nombre: String,
    apellidos: String,
    onAbrirPdf: (String, String) -> Unit,
    onAbrirDetalleLc: (Long) -> Unit
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
            onClickPdf = {
                val file = PdfRevisionesUtils.generarPdfRevisionLc(
                    context = context,
                    nombre = nombre,
                    apellidos = apellidos,
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
            },
            onClickDetalle = {
                onAbrirDetalleLc(item.id_revision_lc.toLong())
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
    onClickPdf: () -> Unit,
    onClickDetalle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = OpticCardBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Fecha: ${formatFecha(fecha)}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "GRADUACIÓN OD",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = od,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "GRADUACIÓN OI",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = oi,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "OPTOMETRISTA",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = optometrista ?: "—",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClickDetalle,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver detalle")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClickPdf,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver receta PDF")
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
