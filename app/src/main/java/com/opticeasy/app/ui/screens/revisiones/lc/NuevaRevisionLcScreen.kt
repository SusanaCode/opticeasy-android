package com.opticeasy.app.ui.screens.revisiones.lc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticCardBg
import com.opticeasy.app.viewmodel.revisiones.lc.NuevaRevisionLcViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

private fun fmt2(v: Double): String = String.format(Locale.US, "%.2f", v)

private fun rangeDoubles(start: Double, end: Double, step: Double): List<String> {
    val out = mutableListOf<String>()
    val nSteps = ((end - start) / step).roundToInt()
    for (i in 0..nSteps) out.add(fmt2(start + i * step))
    return out
}

private fun rangeInts(start: Int, end: Int, step: Int): List<String> =
    (start..end step step).map { it.toString() }

private val OPC_ESFERA_LC = rangeDoubles(-20.00, 20.00, 0.25)
private val OPC_CILINDRO_LC = rangeDoubles(-10.00, 0.00, 0.25).reversed() // 0.00..-10.00 (solo negativos)
private val OPC_EJE_LC = rangeInts(0, 180, 5)
private val OPC_AV_LC = run {
    val out = mutableListOf<String>()
    val n = ((1.0 - 0.0) / 0.05).roundToInt()
    for (i in 0..n) out.add(fmt2(0.0 + i * 0.05)) // 0.00..1.00 step 0.05
    out
}
private val OPC_ADD_LC = rangeDoubles(0.00, 4.00, 0.25) // 0..4.00 step 0.25

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun whiteFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surface,
    errorContainerColor = MaterialTheme.colorScheme.surface
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpinnerField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val normalizedValue = runCatching { fmt2(value.toDouble()) }.getOrNull() ?: value

    val orderedOptions = remember(normalizedValue, options) {
        val nums = options.mapNotNull { it.toDoubleOrNull() }
        if (nums.size != options.size) return@remember options

        val v = normalizedValue.toDoubleOrNull() ?: return@remember options
        val step = if (options.size > 1) {
            abs(nums[1] - nums[0]).takeIf { it > 0 } ?: 0.25
        } else 0.25

        fun f(x: Double) = fmt2(x)

        val out = ArrayList<String>(options.size)

        var k = 1
        while (out.size < options.size) {
            val up = v + k * step
            val s = f(up)
            if (options.contains(s)) out.add(s) else break
            k++
        }
        out.reverse()

        val vStr = f(v)
        if (options.contains(vStr)) out.add(vStr)

        k = 1
        while (out.size < options.size) {
            val down = v - k * step
            val s = f(down)
            if (options.contains(s)) out.add(s) else break
            k++
        }

        options.forEach { if (!out.contains(it)) out.add(it) }

        out
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
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
        ) {
            orderedOptions.forEach { opt ->
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

/* ===================== FECHA ES (HOY) ===================== */

private fun hoyEs(): String =
    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaRevisionLcScreen(
    clienteId: Long,
    nombre: String,
    apellidos: String,
    onBack: () -> Unit,
    onGuardadoOk: () -> Unit
) {
    val vm: NuevaRevisionLcViewModel = viewModel()
    val state by vm.state.collectAsState()


    LaunchedEffect(clienteId) {
        vm.init(clienteId, nombre, apellidos)
    }


    LaunchedEffect(state.ok) {
        if (state.ok) {
            onGuardadoOk()
            vm.limpiarOk()
        }
    }

    val green = MaterialTheme.colorScheme.primary
    val shape16 = RoundedCornerShape(16.dp)
    val fechaHoy = remember { hoyEs() }

    BaseScreen(contentTopPadding = 8.dp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // ===== ENCABEZADO como GAFA =====
            Text(
                text = "Nueva revisión de lentes de contacto",
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
                text = "${state.nombre} ${state.apellidos} · Fecha: $fechaHoy",
                style = MaterialTheme.typography.bodyMedium,
                color = green,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))


            OutlinedTextField(
                value = state.anamnesis,
                onValueChange = vm::updateAnamnesis,
                label = { Text("Anamnesis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = whiteFieldColors()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.otrasPruebas,
                onValueChange = vm::updateOtrasPruebas,
                label = { Text("Otras pruebas") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = whiteFieldColors()
            )

            Spacer(Modifier.height(20.dp))

            // ================== TARJETA: GRADUACIÓN (LC) ==================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = shape16,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Graduación",
                        style = MaterialTheme.typography.titleMedium,
                        color = green
                    )

                    Text("OD", style = MaterialTheme.typography.titleSmall, color = green)
                    GraduacionBlock(
                        esfera = state.esferaOd,
                        cilindro = state.cilindroOd,
                        eje = state.ejeOd,
                        av = state.avOd,
                        add = state.addOd,
                        dominante = state.dominanteOd,
                        tipoLente = state.tipoLenteOd,
                        onEsferaChange = { vm.updateEsferaOd(it) },
                        onCilindroChange = { vm.updateCilindroOd(it) },
                        onEjeChange = { vm.updateEjeOd(it) },
                        onAvChange = { vm.updateAvOd(it) },
                        onAddChange = { vm.updateAddOd(it) },
                        onDominanteChange = { vm.updateDominanteOd(it) },
                        onTipoLenteChange = { vm.updateTipoLenteOd(it) }
                    )

                    Divider(color = green.copy(alpha = 0.25f))

                    Text("OI", style = MaterialTheme.typography.titleSmall, color = green)
                    GraduacionBlock(
                        esfera = state.esferaOi,
                        cilindro = state.cilindroOi,
                        eje = state.ejeOi,
                        av = state.avOi,
                        add = state.addOi,
                        dominante = state.dominanteOi,
                        tipoLente = state.tipoLenteOi,
                        onEsferaChange = { vm.updateEsferaOi(it) },
                        onCilindroChange = { vm.updateCilindroOi(it) },
                        onEjeChange = { vm.updateEjeOi(it) },
                        onAvChange = { vm.updateAvOi(it) },
                        onAddChange = { vm.updateAddOi(it) },
                        onDominanteChange = { vm.updateDominanteOi(it) },
                        onTipoLenteChange = { vm.updateTipoLenteOi(it) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            // ===== BOTONES como GAFA (full width) =====
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { vm.guardar() },
                    enabled = !state.isSaving,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(if (state.isSaving) "Guardando..." else "Guardar")
                }

                OutlinedButton(
                    onClick = onBack,
                    enabled = !state.isSaving,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Atrás")
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GraduacionBlock(
    esfera: Double?,
    cilindro: Double?,
    eje: Int?,
    av: Double?,
    add: Double?,
    dominante: Boolean,
    tipoLente: String,
    onEsferaChange: (Double?) -> Unit,
    onCilindroChange: (Double?) -> Unit,
    onEjeChange: (Int?) -> Unit,
    onAvChange: (Double?) -> Unit,
    onAddChange: (Double?) -> Unit,
    onDominanteChange: (Boolean) -> Unit,
    onTipoLenteChange: (String) -> Unit
) {
    val esferaStr = fmt2(esfera ?: 0.0)
    val cilindroStr = fmt2(cilindro ?: 0.0)
    val ejeStr = (eje ?: 0).toString()
    val avStr = fmt2(av ?: 0.0)
    val addStr = fmt2(add ?: 0.0)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerField(
            label = "Esfera",
            value = esferaStr,
            options = OPC_ESFERA_LC,
            onValueChange = { opt -> onEsferaChange(opt.toDoubleOrNull()) },
            modifier = Modifier.weight(1f)
        )
        SpinnerField(
            label = "Cilindro",
            value = cilindroStr,
            options = OPC_CILINDRO_LC,
            onValueChange = { opt -> onCilindroChange(opt.toDoubleOrNull()) },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerField(
            label = "Eje",
            value = ejeStr,
            options = OPC_EJE_LC,
            onValueChange = { opt -> onEjeChange(opt.toIntOrNull()) },
            modifier = Modifier.weight(1f)
        )

        SpinnerField(
            label = "AV",
            value = avStr,
            options = OPC_AV_LC,
            onValueChange = { opt -> onAvChange(opt.toDoubleOrNull()) },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SpinnerField(
            label = "ADD",
            value = addStr,
            options = OPC_ADD_LC,
            onValueChange = { opt -> onAddChange(opt.toDoubleOrNull()) },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = tipoLente,
            onValueChange = onTipoLenteChange,
            label = { Text("Tipo lente") },
            modifier = Modifier.weight(1f),
            colors = whiteFieldColors()
        )
    }

    Spacer(Modifier.height(8.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dominante")
        Switch(checked = dominante, onCheckedChange = onDominanteChange)
    }
}