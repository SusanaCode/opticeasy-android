package com.opticeasy.app.ui.screens.calculadora

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.opticeasy.app.ui.components.BaseScreen
import java.util.Locale
import kotlin.math.abs
import kotlin.math.round

@Composable
fun CalculadoraLCScreen(
    onMenuPrincipal: () -> Unit
) {
    var vertexMm by rememberSaveable { mutableStateOf("12") }
    var odS by rememberSaveable { mutableStateOf("") }
    var odC by rememberSaveable { mutableStateOf("") }
    var odA by rememberSaveable { mutableStateOf("") }
    var oiS by rememberSaveable { mutableStateOf("") }
    var oiC by rememberSaveable { mutableStateOf("") }
    var oiA by rememberSaveable { mutableStateOf("") }
    var resOd by rememberSaveable { mutableStateOf("") }
    var resOi by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val green = MaterialTheme.colorScheme.primary
    val shape16 = RoundedCornerShape(16.dp)

    BaseScreen(contentTopPadding = 8.dp) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Calculadora LC",
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

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Introduce la graduación de gafa para calcular la potencia equivalente en lente de contacto.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = vertexMm,
            onValueChange = { vertexMm = it.filter(Char::isDigit) },
            label = { Text("Distancia vértice (mm)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = fieldColors()
        )

        Spacer(Modifier.height(16.dp))

        Text("OD", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoNumero("Esfera", odS, { odS = normalize(it) }, Modifier.weight(1f))
            CampoNumero("Cilindro", odC, { odC = normalize(it) }, Modifier.weight(1f))
            CampoNumero(
                "Eje",
                odA,
                { odA = it.filter(Char::isDigit).take(3) },
                Modifier.weight(1f),
                KeyboardType.Number
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("OI", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoNumero("Esfera", oiS, { oiS = normalize(it) }, Modifier.weight(1f))
            CampoNumero("Cilindro", oiC, { oiC = normalize(it) }, Modifier.weight(1f))
            CampoNumero(
                "Eje",
                oiA,
                { oiA = it.filter(Char::isDigit).take(3) },
                Modifier.weight(1f),
                KeyboardType.Number
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    error = null

                    val d = (vertexMm.toDoubleOrNull() ?: 12.0) / 1000.0

                    val od = calcularLC(
                        odS.toDoubleOrNull(),
                        odC.toDoubleOrNull(),
                        odA.toIntOrNull(),
                        d
                    )

                    val oi = calcularLC(
                        oiS.toDoubleOrNull(),
                        oiC.toDoubleOrNull(),
                        oiA.toIntOrNull(),
                        d
                    )

                    resOd = od?.formato() ?: ""
                    resOi = oi?.formato() ?: ""

                    if (resOd.isBlank() && resOi.isBlank()) {
                        error = "Introduce al menos un ojo válido"
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Calcular")
            }

            OutlinedButton(
                onClick = {
                    odS = ""; odC = ""; odA = ""
                    oiS = ""; oiC = ""; oiA = ""
                    resOd = ""; resOi = ""
                    error = null
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Borrar")
            }
        }

        if (error != null) {
            Spacer(Modifier.height(8.dp))

            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(20.dp))

        Text("LC recomendada", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = resOd,
            onValueChange = {},
            readOnly = true,
            label = { Text("OD") },
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = resOi,
            onValueChange = {},
            readOnly = true,
            label = { Text("OI") },
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors()
        )

        Spacer(Modifier.height(24.dp))
        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onMenuPrincipal,
                enabled = true,
                shape = shape16,
                modifier = Modifier.height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Menú principal")
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

}
@Composable
private fun CampoNumero(
    label: String,
    valor: String,
    onChange: (String) -> Unit,
    modifier: Modifier,
    tipo: KeyboardType = KeyboardType.Decimal
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = tipo),
        colors = fieldColors()
    )
}

@Composable
private fun fieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface
    )

private fun normalize(input: String): String {
    val s = input.replace(",", ".")

    return buildString {
        s.forEachIndexed { index, c ->
            if (c.isDigit() || c == '.' || (c == '-' && index == 0)) append(c)
        }
    }
}

private data class Rx(val esfera: Double, val cilindro: Double, val eje: Int) {
    fun formato(): String {
        return "${formatoPot(esfera)} ${formatoPot(cilindro)} x ${eje.toString().padStart(3, '0')}"
    }
}

private fun calcularLC(
    esfera: Double?,
    cilindro: Double?,
    eje: Int?,
    d: Double
): Rx? {
    if (esfera == null) return null

    val c = cilindro ?: 0.0
    val ax = eje ?: 0
    val f1 = esfera
    val f2 = esfera + c
    val f1c = compensarVertice(f1, d)
    val f2c = compensarVertice(f2, d)
    val nuevaEsfera = redondear(f1c)
    val nuevoCil = redondear(f2c - f1c)

    return Rx(nuevaEsfera, nuevoCil, ax)
}

private fun compensarVertice(f: Double, d: Double): Double {
    return f / (1 - d * f)
}

private fun redondear(x: Double): Double {
    return round(x * 4) / 4
}

private fun formatoPot(x: Double): String {
    val signo = if (x >= 0) "+" else "-"
    return signo + String.format(Locale.US, "%.2f", abs(x))
}