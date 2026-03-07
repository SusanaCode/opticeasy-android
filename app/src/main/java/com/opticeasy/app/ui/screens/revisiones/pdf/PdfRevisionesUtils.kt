package com.opticeasy.app.ui.screens.revisiones.pdf


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import com.opticeasy.app.R
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.print.PrintAttributes
import android.print.PrintManager
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.os.ParcelFileDescriptor


object PdfRevisionesUtils {
    fun generarPdfRevisionGafa(
        context: Context,
        nombre: String,
        apellidos: String,
        codigoCliente: String?,
        fechaRevisionYYYYMMDD: String,
        od: RevisionOjoPdf,
        oi: RevisionOjoPdf,
        optometrista: String?,
        numeroColegiado: Int?
    ): File {
        val titulo = "Informe de revisión - Gafa"
        val fileName = "revision_gafa_${fechaRevisionYYYYMMDD}_${safe(nombre)}_${safe(apellidos)}.pdf"
        return generarPdf(
            context = context,
            fileName = fileName,
            titulo = titulo,
            nombre = nombre,
            apellidos = apellidos,
            codigoCliente = codigoCliente,
            fechaRevisionYYYYMMDD = fechaRevisionYYYYMMDD,
            od = od,
            oi = oi,
            optometrista = optometrista,
            numeroColegiado = numeroColegiado
        )
    }

    fun generarPdfRevisionLc(
        context: Context,
        nombre: String,
        apellidos: String,
        codigoCliente: String?,
        fechaRevisionYYYYMMDD: String,
        od: RevisionOjoPdf,
        oi: RevisionOjoPdf,
        optometrista: String?,
        numeroColegiado: Int?
    ): File {
        val titulo = "Informe de revisión - Lentes de contacto"
        val fileName = "revision_lc_${fechaRevisionYYYYMMDD}_${safe(nombre)}_${safe(apellidos)}.pdf"
        return generarPdf(
            context = context,
            fileName = fileName,
            titulo = titulo,
            nombre = nombre,
            apellidos = apellidos,
            codigoCliente = codigoCliente,
            fechaRevisionYYYYMMDD = fechaRevisionYYYYMMDD,
            od = od,
            oi = oi,
            optometrista = optometrista,
            numeroColegiado = numeroColegiado
        )
    }



    data class RevisionOjoPdf(
        val esfera: String?,
        val cilindro: String?,
        val eje: String?,
        val av: String?,
        val add: String?
    )

    //---------- Imprimimos el PDF ----------

    fun imprimirPdf(context: Context, file: File) {

        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        val adapter = object : PrintDocumentAdapter() {

            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: android.os.Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback?.onLayoutCancelled()
                    return
                }

                val info = PrintDocumentInfo.Builder(file.name)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()

                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<android.print.PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: WriteResultCallback?
            ) {

                try {
                    file.inputStream().use { input ->
                        destination?.fileDescriptor?.let { fd ->
                            fd.let {
                                java.io.FileOutputStream(it).use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    }

                    callback?.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))

                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                }
            }
        }

        printManager.print(
            "Informe revisión",
            adapter,
            PrintAttributes.Builder().build()
        )
    }

    // ---------- Implementación ----------

    private fun generarPdf(
        context: Context,
        fileName: String,
        titulo: String,
        nombre: String,
        apellidos: String,
        codigoCliente: String?,
        fechaRevisionYYYYMMDD: String,
        od: RevisionOjoPdf,
        oi: RevisionOjoPdf,
        optometrista: String?,
        numeroColegiado: Int?
    ): File {
        val pdf = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLACK }
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#D0D0D0")
            strokeWidth = 2f
        }

        val margin = 40f
        var y = margin

        canvas.drawColor(Color.WHITE)

        y = dibujarLogo(context, canvas, margin, y)

        textPaint.textSize = 18f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(titulo, margin, y, textPaint)
        y += 20f

        canvas.drawLine(margin, y, pageInfo.pageWidth - margin, y, linePaint)
        y += 18f

        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        canvas.drawText("Paciente: $nombre $apellidos", margin, y, textPaint)
        y += 16f

        canvas.drawText("Fecha revisión: ${formatFecha(fechaRevisionYYYYMMDD)}", margin, y, textPaint)
        y += 60f

        y = dibujarBloqueOjo(canvas, textPaint, linePaint, "OD", od, margin, y, pageInfo.pageWidth - margin)
        y += 12f
        y = dibujarBloqueOjo(canvas, textPaint, linePaint, "OI", oi, margin, y, pageInfo.pageWidth - margin)
        y += 18f

        val footerTop = pageInfo.pageHeight - 140f

        canvas.drawLine(
            margin,
            footerTop,
            pageInfo.pageWidth - margin,
            footerTop,
            linePaint
        )

        var footerY = footerTop + 22f

        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        val optoTexto = if (numeroColegiado != null) {
            "Optometrista: ${optometrista ?: "—"}  Nº Col.: $numeroColegiado"
        } else {
            "Optometrista: ${optometrista ?: "—"}"
        }

        canvas.drawText(
            optoTexto,
            margin,
            footerY,
            textPaint
        )

        footerY += 36f

        canvas.drawText("Firma:", margin, footerY, textPaint)

        canvas.drawLine(
            margin,
            footerY + 28f,
            pageInfo.pageWidth - margin,
            footerY + 28f,
            linePaint
        )

        pdf.finishPage(page)

        val outFile = File(context.cacheDir, fileName)
        FileOutputStream(outFile).use { fos -> pdf.writeTo(fos) }
        pdf.close()

        return outFile
    }

    private fun dibujarBloqueOjo(
        canvas: Canvas,
        textPaint: TextPaint,
        linePaint: Paint,
        titulo: String,
        ojo: RevisionOjoPdf,
        left: Float,
        top: Float,
        right: Float
    ): Float {
        var y = top

        textPaint.textSize = 13f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Graduación $titulo", left, y, textPaint)
        y += 14f

        val boxTop = y
        val boxBottom = y + 56f
        val rect = RectF(left, boxTop, right, boxBottom)
        val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#F4F7F6") }
        canvas.drawRoundRect(rect, 10f, 10f, fill)
        canvas.drawRoundRect(rect, 10f, 10f, linePaint)

        textPaint.textSize = 12f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        val vEsf = ojo.esfera ?: "—"
        val vCil = ojo.cilindro ?: "—"
        val vEje = ojo.eje ?: "—"
        val vAv = ojo.av ?: "—"
        val vAdd = ojo.add ?: "—"

        val line1 = "ESF: $vEsf    CIL: $vCil    EJE: $vEje"
        val line2 = "AV:  $vAv     ADD: $vAdd"

        canvas.drawText(line1, left + 14f, boxTop + 22f, textPaint)
        canvas.drawText(line2, left + 14f, boxTop + 44f, textPaint)

        return boxBottom + 18f
    }

    private fun dibujarLogo(context: Context, canvas: Canvas, x: Float, y: Float): Float {
        return try {
            val opts = BitmapFactory.Options().apply {
                inScaled = false
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }

            val bmp = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.logo_opticeasy_pdf,
                opts
            ) ?: return y

            val targetWidth = 130f
            val ratio = bmp.height.toFloat() / bmp.width.toFloat()
            val targetHeight = targetWidth * ratio

            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                isFilterBitmap = true
            }

            val dst = RectF(x, y, x + targetWidth, y + targetHeight)
            canvas.drawBitmap(bmp, null, dst, paint)

            y + targetHeight + 30f

        } catch (_: Exception) {
            y
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

    private fun safe(s: String): String =
        s.lowercase()
            .replace(" ", "_")
            .replace(Regex("[^a-z0-9_áéíóúñ]"), "")
}