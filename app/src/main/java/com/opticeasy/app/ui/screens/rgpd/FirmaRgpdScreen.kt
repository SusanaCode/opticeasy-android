package com.opticeasy.app.ui.screens.rgpd

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticCardBg
import com.opticeasy.app.viewmodel.rgpd.FirmaRgpdUiState
import com.opticeasy.app.viewmodel.rgpd.FirmaRgpdViewModel
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.hypot

@Composable
fun FirmaRgpdScreen(
    clienteId: Int,
    onBack: () -> Unit,
    onFirmaOk: (Int) -> Unit,
    vm: FirmaRgpdViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    val scroll = rememberScrollState()

    var strokes by rememberSaveable { mutableStateOf(listOf<List<Float>>()) }
    var currentStroke by rememberSaveable { mutableStateOf<List<Float>?>(null) }

    // Para saber tamaño del canvas y exportar bitmap con buena resolución
    var padWidthPx by rememberSaveable { mutableStateOf(0) }
    var padHeightPx by rememberSaveable { mutableStateOf(0) }

    // checkbox de aceptación
    var acepto by rememberSaveable { mutableStateOf(false) }

    val loading = state is FirmaRgpdUiState.Loading
    val error = (state as? FirmaRgpdUiState.Error)?.message
    val okMsg = (state as? FirmaRgpdUiState.Success)?.message

    LaunchedEffect(state) {
        if (state is FirmaRgpdUiState.Success) {
            onFirmaOk(clienteId)
        }
    }

    val hoy = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))
    }

    val textoRgpd = remember {
        """
        INFORMACIÓN BÁSICA DE PROTECCIÓN DE DATOS (RGPD)

        Responsable del tratamiento:
        OpticEasy (Centro Óptico) — (rellenar razón social, CIF y dirección)

        Finalidad:
        Gestionar su historial de cliente, citas y revisiones optométricas, elaboración de presupuestos, facturación,
        atención postventa y, si procede, adaptación y seguimiento de productos sanitarios (gafas/lentes de contacto).

        Legitimación:
        Ejecución de la relación contractual/precontractual y cumplimiento de obligaciones legales.
        En caso de comunicaciones comerciales, su consentimiento (si se marcara expresamente).

        Destinatarios:
        No se cederán datos a terceros salvo obligación legal o cuando sea necesario para la prestación del servicio
        (por ejemplo, laboratorios/proveedores) bajo los correspondientes acuerdos.

        Derechos:
        Acceder, rectificar y suprimir los datos, así como otros derechos, como limitación u oposición,
        y portabilidad, en los términos establecidos en la normativa, dirigiéndose al responsable del tratamiento.

        Conservación:
        Los datos se conservarán mientras dure la relación y durante los plazos exigidos por la normativa aplicable.

        Declaro haber leído y comprendido la información anterior y consiento el tratamiento de mis datos
        para las finalidades indicadas.

        Fecha: $hoy
        Cliente ID (interno): $clienteId
        """.trimIndent()
    }

    val buttonShape = RoundedCornerShape(16.dp)

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Firma RGPD",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = OpticCardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Texto legal",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = textoRgpd,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = acepto,
                    onCheckedChange = { acepto = it },
                    enabled = !loading
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "He leído y acepto el tratamiento de mis datos.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Firma del cliente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            SignaturePad(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline),
                enabled = !loading,
                strokes = strokes,
                onStartPath = { start ->
                    val nuevo = listOf(start.x, start.y)
                    currentStroke = nuevo
                    strokes = strokes + listOf(nuevo)
                },
                onMove = { point ->
                    val actual = currentStroke ?: return@SignaturePad
                    val actualizado = actual + listOf(point.x, point.y)
                    currentStroke = actualizado
                    if (strokes.isNotEmpty()) {
                        strokes = strokes.dropLast(1) + listOf(actualizado)
                    }
                },
                onEnd = {
                    currentStroke = null
                },
                onSizePx = { w, h ->
                    padWidthPx = w
                    padHeightPx = h
                }
            )

            Spacer(Modifier.height(12.dp))

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            if (okMsg != null) {
                Text(
                    text = okMsg,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        strokes = emptyList()
                        currentStroke = null
                        vm.reset()
                    },
                    enabled = !loading,
                    shape = buttonShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Limpiar")
                }

                Button(
                    onClick = {
                        vm.reset()

                        if (!acepto) {
                            vm.setError("Debes aceptar el texto legal antes de firmar.")
                            return@Button
                        }

                        if (strokes.isEmpty()) {
                            vm.setError("La firma está vacía.")
                            return@Button
                        }

                        val pngBase64 = exportSignatureToBase64Png(
                            widthPx = padWidthPx,
                            heightPx = padHeightPx,
                            strokes = strokes
                        )

                        if (pngBase64 == null) {
                            vm.setError("No se pudo generar la imagen de la firma.")
                            return@Button
                        }

                        vm.guardarFirma(clienteId = clienteId, imagenBase64Png = pngBase64)
                    },
                    enabled = !loading,
                    shape = buttonShape,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Guardar firma")
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                enabled = !loading,
                shape = buttonShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Atrás")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SignaturePad(
    modifier: Modifier,
    enabled: Boolean,
    strokes: List<List<Float>>,
    onStartPath: (Offset) -> Unit,
    onMove: (Offset) -> Unit,
    onEnd: () -> Unit,
    onSizePx: (Int, Int) -> Unit
) {
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var lastPoint by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = modifier
            .background(Color.White)
            .pointerInput(enabled, canvasSize) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = { start ->
                        val clamped = Offset(
                            x = start.x.coerceIn(0f, canvasSize.width),
                            y = start.y.coerceIn(0f, canvasSize.height)
                        )
                        lastPoint = clamped
                        onStartPath(clamped)
                    },
                    onDrag = { change, _ ->
                        val p = change.position
                        val clamped = Offset(
                            x = p.x.coerceIn(0f, canvasSize.width),
                            y = p.y.coerceIn(0f, canvasSize.height)
                        )

                        val lp = lastPoint
                        val dist = if (lp == null) Float.MAX_VALUE
                        else hypot((clamped.x - lp.x), (clamped.y - lp.y))

                        if (dist > 2.5f) {
                            onMove(clamped)
                            lastPoint = clamped
                        }
                    },
                    onDragEnd = {
                        lastPoint = null
                        onEnd()
                    },
                    onDragCancel = {
                        lastPoint = null
                        onEnd()
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            canvasSize = size
            onSizePx(size.width.toInt(), size.height.toInt())

            strokes.forEach { strokePoints ->
                val path = pointsToPath(strokePoints)
                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(
                        width = 6f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

private fun pointsToPath(points: List<Float>): Path {
    val path = Path()
    if (points.size >= 2) {
        path.moveTo(points[0], points[1])
        var i = 2
        while (i + 1 < points.size) {
            path.lineTo(points[i], points[i + 1])
            i += 2
        }
    }
    return path
}

// Exporta el canvas de firma a PNG Base64. Devuelve SOLO el Base64.
private fun exportSignatureToBase64Png(
    widthPx: Int,
    heightPx: Int,
    strokes: List<List<Float>>
): String? {
    if (widthPx <= 0 || heightPx <= 0) return null

    val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    canvas.drawColor(android.graphics.Color.WHITE)

    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 8f
        style = android.graphics.Paint.Style.STROKE
        strokeCap = android.graphics.Paint.Cap.ROUND
        strokeJoin = android.graphics.Paint.Join.ROUND
        isAntiAlias = true
    }

    try {
        strokes.forEach { strokePoints ->
            val composePath = pointsToPath(strokePoints)
            val androidPath = composePath.asAndroidPath()
            canvas.drawPath(androidPath, paint)
        }
    } catch (_: Throwable) {
        return null
    }

    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val bytes = baos.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}