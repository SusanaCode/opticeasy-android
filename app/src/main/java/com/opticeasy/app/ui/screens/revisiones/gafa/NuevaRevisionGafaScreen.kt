package com.opticeasy.app.ui.screens.revisiones.gafa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticCardBg
import com.opticeasy.app.viewmodel.revisiones.gafa.NuevaRevisionGafaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

/* ===================== FORMATOS CON SIGNO ===================== */

private fun fmtSigned2(v: Double): String {
    val sign = if (v >= 0) "+" else ""
    return sign + String.format(Locale.US, "%.2f", v)
}

private fun fmtSigned1(v: Double): String {
    val sign = if (v >= 0) "+" else ""
    return sign + String.format(Locale.US, "%.1f", v)
}

private fun rangeSignedDoubles(start: Double, end: Double, step: Double): List<String> {
    val out = mutableListOf<String>()
    val nSteps = ((end - start) / step).roundToInt()
    for (i in 0..nSteps) out.add(fmtSigned2(start + i * step))
    return out
}

private fun rangeInts(start: Int, end: Int, step: Int): List<String> =
    (start..end step step).map { it.toString() }

/* ===================== OPCIONES ===================== */


private val OPC_ESFERA = rangeSignedDoubles(-30.00, 15.00, 0.25).reversed()

private val OPC_CILINDRO = rangeSignedDoubles(-10.00, 0.00, 0.25).reversed()

private val OPC_EJE = rangeInts(0, 180, 5)

// AV: +0.0..+1.0
private val OPC_AV = (0..10).map { fmtSigned1(it / 10.0) }

private val OPC_ADD = rangeSignedDoubles(0.00, 3.50, 0.25)
private val OPC_CCF = rangeSignedDoubles(0.00, 3.50, 0.25)
private val OPC_ARN = rangeSignedDoubles(-3.00, 0.00, 0.25).reversed() // +0.00..-3.00
private val OPC_ARP = rangeSignedDoubles(0.00, 3.00, 0.25)

private val OPC_PRISMA = run {
    val out = mutableListOf<String>()
    val n = ((20.0 - 0.0) / 0.5).roundToInt()
    for (i in 0..n) out.add(fmtSigned1(0.0 + i * 0.5))
    out
}

/* ===================== FECHA ES ===================== */

private fun fechaEsFromIsoOrSame(value: String): String {
    return try {
        val d = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE) // YYYY-MM-DD
        d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))
    } catch (_: Throwable) {
        value
    }
}

/* ===================== COLORES CAMPOS ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun whiteFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surface,
    errorContainerColor = MaterialTheme.colorScheme.surface
)

/* ===================== SPINNER (LC) ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpinnerFieldLcStyle(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = whiteFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = OpticCardBg,
            tonalElevation = 0.dp
        )  {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onValueChange(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

/* ===================== SPINNER ESFERA  ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpinnerFieldEsferaLcStyle(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val selectedIndex = remember(value, options) {
        options.indexOf(value).let { if (it >= 0) it else 0 }
    }

    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val itemHeightPx = with(density) { 48.dp.toPx() }.toInt()

    LaunchedEffect(expanded, selectedIndex) {
        if (expanded) {
            val targetIndex = (selectedIndex - 2).coerceAtLeast(0)
            scrollState.scrollTo(targetIndex * itemHeightPx)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = whiteFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = OpticCardBg,
            tonalElevation = 0.dp
        )  {
            Column(
                modifier = Modifier
                    .heightIn(max = 320.dp)
                    .verticalScroll(scrollState)
            ) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            onValueChange(opt)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

/* ===================== BLOQUES GRADUACIÓN (LC) ===================== */

@Composable
private fun GraduacionUsadaBlock(
    esfera: String,
    cilindro: String,
    eje: String,
    av: String,
    onEsferaChange: (String) -> Unit,
    onCilindroChange: (String) -> Unit,
    onEjeChange: (String) -> Unit,
    onAvChange: (String) -> Unit,
    enabled: Boolean
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldEsferaLcStyle(
            label = "Esfera",
            value = esfera,
            options = OPC_ESFERA,
            onValueChange = onEsferaChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        SpinnerFieldLcStyle(
            label = "Cilindro",
            value = cilindro,
            options = OPC_CILINDRO,
            onValueChange = onCilindroChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldLcStyle(
            label = "Eje",
            value = eje,
            options = OPC_EJE,
            onValueChange = onEjeChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        SpinnerFieldLcStyle(
            label = "AV",
            value = av,
            options = OPC_AV,
            onValueChange = onAvChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }
}

@Composable
private fun GraduacionNuevaBlock(
    esfera: String,
    cilindro: String,
    eje: String,
    av: String,
    add: String,
    prisma: String,
    ccf: String,
    arn: String,
    arp: String,
    dominante: Boolean,
    onEsferaChange: (String) -> Unit,
    onCilindroChange: (String) -> Unit,
    onEjeChange: (String) -> Unit,
    onAvChange: (String) -> Unit,
    onAddChange: (String) -> Unit,
    onPrismaChange: (String) -> Unit,
    onCcfChange: (String) -> Unit,
    onArnChange: (String) -> Unit,
    onArpChange: (String) -> Unit,
    onDominanteChange: (Boolean) -> Unit,
    enabled: Boolean
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldEsferaLcStyle(
            label = "Esfera",
            value = esfera,
            options = OPC_ESFERA,
            onValueChange = onEsferaChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        SpinnerFieldLcStyle(
            label = "Cilindro",
            value = cilindro,
            options = OPC_CILINDRO,
            onValueChange = onCilindroChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldLcStyle(
            label = "Eje",
            value = eje,
            options = OPC_EJE,
            onValueChange = onEjeChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        SpinnerFieldLcStyle(
            label = "AV",
            value = av,
            options = OPC_AV,
            onValueChange = onAvChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldLcStyle(
            label = "ADD",
            value = add,
            options = OPC_ADD,
            onValueChange = onAddChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        SpinnerFieldLcStyle(
            label = "Prisma",
            value = prisma,
            options = OPC_PRISMA,
            onValueChange = onPrismaChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldLcStyle(
            label = "CCF",
            value = ccf,
            options = OPC_CCF,
            onValueChange = onCcfChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        SpinnerFieldLcStyle(
            label = "ARN",
            value = arn,
            options = OPC_ARN,
            onValueChange = onArnChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerFieldLcStyle(
            label = "ARP",
            value = arp,
            options = OPC_ARP,
            onValueChange = onArpChange,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
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
            onCheckedChange = { if (enabled) onDominanteChange(it) },
            enabled = enabled
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaRevisionGafaScreen(
    vm: NuevaRevisionGafaViewModel,
    onVolver: () -> Unit,

    ) {
    val state by vm.state.collectAsState()

    val green = MaterialTheme.colorScheme.primary
    val shape16 = RoundedCornerShape(16.dp)

    val normalFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface
    )

    val fechaEs = remember(state.fechaRevision) { fechaEsFromIsoOrSame(state.fechaRevision) }

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Nueva revisión de gafa",
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
                text = "${state.nombre} ${state.apellidos} · Fecha: $fechaEs",
                style = MaterialTheme.typography.bodyMedium,
                color = green,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = state.anamnesis,
                onValueChange = { v -> vm.update { it.copy(anamnesis = v) } },
                label = { Text("Anamnesis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = normalFieldColors
            )

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = shape16,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Graduación usada", style = MaterialTheme.typography.titleMedium, color = green)

                    Text("OD", style = MaterialTheme.typography.titleSmall, color = green)
                    GraduacionUsadaBlock(
                        esfera = state.usadaOD.esfera,
                        cilindro = state.usadaOD.cilindro,
                        eje = state.usadaOD.eje,
                        av = state.usadaOD.av,
                        onEsferaChange = { v -> vm.update { st -> st.copy(usadaOD = st.usadaOD.copy(esfera = v)) } },
                        onCilindroChange = { v -> vm.update { st -> st.copy(usadaOD = st.usadaOD.copy(cilindro = v)) } },
                        onEjeChange = { v -> vm.update { st -> st.copy(usadaOD = st.usadaOD.copy(eje = v)) } },
                        onAvChange = { v -> vm.update { st -> st.copy(usadaOD = st.usadaOD.copy(av = v)) } },
                        enabled = !state.loading
                    )

                    Spacer(Modifier.height(6.dp))

                    Text("OI", style = MaterialTheme.typography.titleSmall, color = green)
                    GraduacionUsadaBlock(
                        esfera = state.usadaOI.esfera,
                        cilindro = state.usadaOI.cilindro,
                        eje = state.usadaOI.eje,
                        av = state.usadaOI.av,
                        onEsferaChange = { v -> vm.update { st -> st.copy(usadaOI = st.usadaOI.copy(esfera = v)) } },
                        onCilindroChange = { v -> vm.update { st -> st.copy(usadaOI = st.usadaOI.copy(cilindro = v)) } },
                        onEjeChange = { v -> vm.update { st -> st.copy(usadaOI = st.usadaOI.copy(eje = v)) } },
                        onAvChange = { v -> vm.update { st -> st.copy(usadaOI = st.usadaOI.copy(av = v)) } },
                        enabled = !state.loading
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = shape16,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Nueva graduación", style = MaterialTheme.typography.titleMedium, color = green)

                    Text("OD", style = MaterialTheme.typography.titleSmall, color = green)
                    GraduacionNuevaBlock(
                        esfera = state.nuevaOD.esfera,
                        cilindro = state.nuevaOD.cilindro,
                        eje = state.nuevaOD.eje,
                        av = state.nuevaOD.av,
                        add = state.nuevaOD.add,
                        prisma = state.nuevaOD.prisma,
                        ccf = state.nuevaOD.ccf,
                        arn = state.nuevaOD.arn,
                        arp = state.nuevaOD.arp,
                        dominante = state.nuevaOD.dominante,
                        onEsferaChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(esfera = v)) } },
                        onCilindroChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(cilindro = v)) } },
                        onEjeChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(eje = v)) } },
                        onAvChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(av = v)) } },
                        onAddChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(add = v)) } },
                        onPrismaChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(prisma = v)) } },
                        onCcfChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(ccf = v)) } },
                        onArnChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(arn = v)) } },
                        onArpChange = { v -> vm.update { st -> st.copy(nuevaOD = st.nuevaOD.copy(arp = v)) } },
                        onDominanteChange = { checked ->
                            vm.update { st ->
                                st.copy(
                                    nuevaOD = st.nuevaOD.copy(dominante = checked),
                                    nuevaOI = st.nuevaOI.copy(dominante = if (checked) false else st.nuevaOI.dominante)
                                )
                            }
                        },
                        enabled = !state.loading
                    )

                    Divider(color = green.copy(alpha = 0.25f))

                    Text("OI", style = MaterialTheme.typography.titleSmall, color = green)
                    GraduacionNuevaBlock(
                        esfera = state.nuevaOI.esfera,
                        cilindro = state.nuevaOI.cilindro,
                        eje = state.nuevaOI.eje,
                        av = state.nuevaOI.av,
                        add = state.nuevaOI.add,
                        prisma = state.nuevaOI.prisma,
                        ccf = state.nuevaOI.ccf,
                        arn = state.nuevaOI.arn,
                        arp = state.nuevaOI.arp,
                        dominante = state.nuevaOI.dominante,
                        onEsferaChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(esfera = v)) } },
                        onCilindroChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(cilindro = v)) } },
                        onEjeChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(eje = v)) } },
                        onAvChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(av = v)) } },
                        onAddChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(add = v)) } },
                        onPrismaChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(prisma = v)) } },
                        onCcfChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(ccf = v)) } },
                        onArnChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(arn = v)) } },
                        onArpChange = { v -> vm.update { st -> st.copy(nuevaOI = st.nuevaOI.copy(arp = v)) } },
                        onDominanteChange = { checked ->
                            vm.update { st ->
                                st.copy(
                                    nuevaOI = st.nuevaOI.copy(dominante = checked),
                                    nuevaOD = st.nuevaOD.copy(dominante = if (checked) false else st.nuevaOD.dominante)
                                )
                            }
                        },
                        enabled = !state.loading
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.otrasPruebas,
                onValueChange = { v -> vm.update { it.copy(otrasPruebas = v) } },
                label = { Text("Otras pruebas de interés") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = normalFieldColors
            )

            Spacer(Modifier.height(12.dp))

            if (state.error != null) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
            }
            if (state.savedOk) {
                Text("Guardado correctamente ✅", color = green)
            }

            Spacer(Modifier.height(12.dp))

            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { vm.guardar() },
                    enabled = !state.loading,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(if (state.loading) "Guardando..." else "Guardar")
                }

                OutlinedButton(
                    onClick = onVolver,
                    enabled = !state.loading,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) { Text("Atrás") }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}