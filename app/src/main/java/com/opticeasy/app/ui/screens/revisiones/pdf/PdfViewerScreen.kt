package com.opticeasy.app.ui.screens.revisiones.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticCardBg
import java.io.File

@Composable
fun PdfViewerScreen(
    pdfPath: String,
    titulo: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val pdfFile = remember(pdfPath) { File(pdfPath) }
    val shape16 = RoundedCornerShape(16.dp)
    val green = MaterialTheme.colorScheme.primary

    val bitmaps by remember(pdfPath) {
        mutableStateOf(renderPdfPages(pdfPath))
    }

    BaseScreen(contentTopPadding = 8.dp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = titulo,
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

            Spacer(Modifier.height(16.dp))

            if (bitmaps.isEmpty()) {
                Text(
                    text = "No se pudo cargar el PDF",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                bitmaps.forEachIndexed { index, bitmap ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = OpticCardBg
                        )
                    ){
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Página ${index + 1}",
                                style = MaterialTheme.typography.labelMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Página ${index + 1} del PDF",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    PdfRevisionesUtils.imprimirPdf(context, pdfFile)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = shape16
            ) {
                Text("Imprimir PDF")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = shape16
            ) {
                Text("Volver")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

private fun renderPdfPages(pdfPath: String): List<Bitmap> {
    val file = File(pdfPath)
    if (!file.exists()) return emptyList()

    val result = mutableListOf<Bitmap>()
    var descriptor: ParcelFileDescriptor? = null
    var renderer: PdfRenderer? = null

    try {
        descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        renderer = PdfRenderer(descriptor)

        for (i in 0 until renderer.pageCount) {
            val page = renderer.openPage(i)

            val width = page.width * 2
            val height = page.height * 2

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(android.graphics.Color.WHITE)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            result.add(bitmap)
        }
    } catch (_: Exception) {
        return emptyList()
    } finally {
        renderer?.close()
        descriptor?.close()
    }

    return result
}

