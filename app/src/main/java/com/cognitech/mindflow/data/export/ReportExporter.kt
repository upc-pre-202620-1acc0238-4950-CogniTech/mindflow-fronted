package com.cognitech.mindflow.data.export

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.cognitech.mindflow.data.model.JournalEntry
import com.cognitech.mindflow.data.model.Sentiment
import com.cognitech.mindflow.data.model.User
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Genera los reportes clínicos (PDF / CSV) y abre el menú de compartir de Android. */
object ReportExporter {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun shareCsv(context: Context, user: User, entries: List<JournalEntry>) {
        val file = reportFile(context, "mindflow_reporte.csv")
        file.bufferedWriter().use { out ->
            out.write("fecha,categoria,sentimiento,titulo,contenido,respuesta_ia\n")
            entries.forEach { e ->
                out.write(
                    listOf(
                        dateFormat.format(Date(e.createdAt)), e.category, Sentiment.label(e.sentiment),
                        e.title, e.content, e.aiResponse.orEmpty(),
                    ).joinToString(",") { it.csv() } + "\n"
                )
            }
        }
        share(context, file, "text/csv", "Reporte MindFlow de ${user.name}")
    }

    fun sharePdf(context: Context, user: User, entries: List<JournalEntry>) {
        val file = reportFile(context, "mindflow_reporte.pdf")
        val document = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margin = 48f
        val title = Paint().apply { textSize = 20f; typeface = Typeface.DEFAULT_BOLD }
        val heading = Paint().apply { textSize = 12f; typeface = Typeface.DEFAULT_BOLD }
        val body = Paint().apply { textSize = 10.5f }
        val muted = Paint().apply { textSize = 9.5f; color = 0xFF828282.toInt() }

        var pageNumber = 1
        var page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
        var y = margin

        fun newPageIfNeeded(space: Float) {
            if (y + space > pageHeight - margin) {
                document.finishPage(page)
                pageNumber++
                page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
                y = margin
            }
        }

        fun drawWrapped(text: String, paint: Paint, lineHeight: Float) {
            wrap(text, paint, pageWidth - margin * 2).forEach { line ->
                newPageIfNeeded(lineHeight)
                page.canvas.drawText(line, margin, y, paint)
                y += lineHeight
            }
        }

        drawWrapped("Reporte Clínico MindFlow", title, 26f)
        drawWrapped("Paciente: ${user.name} (${user.email})", body, 15f)
        drawWrapped("Generado: ${dateFormat.format(Date())} · ${entries.size} registros", muted, 20f)
        val counts = entries.groupingBy { it.sentiment }.eachCount()
        drawWrapped(
            "Positivo: ${counts[Sentiment.POSITIVE] ?: 0}   Neutral: ${counts[Sentiment.NEUTRAL] ?: 0}   Negativo: ${counts[Sentiment.NEGATIVE] ?: 0}",
            body, 24f,
        )
        entries.forEach { e ->
            newPageIfNeeded(60f)
            drawWrapped("${dateFormat.format(Date(e.createdAt))} · ${e.category} · ${Sentiment.label(e.sentiment)}", muted, 14f)
            drawWrapped(e.title, heading, 16f)
            drawWrapped(e.content, body, 14f)
            e.aiResponse?.let { drawWrapped("MindFlow AI: $it", muted, 13f) }
            y += 12f
        }
        document.finishPage(page)
        file.outputStream().use(document::writeTo)
        document.close()
        share(context, file, "application/pdf", "Reporte clínico MindFlow de ${user.name}")
    }

    private fun wrap(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        text.split("\n").forEach { paragraph ->
            var line = ""
            paragraph.split(" ").forEach { word ->
                val candidate = if (line.isEmpty()) word else "$line $word"
                if (paint.measureText(candidate) <= maxWidth) line = candidate
                else {
                    if (line.isNotEmpty()) lines += line
                    line = word
                }
            }
            lines += line
        }
        return lines
    }

    private fun reportFile(context: Context, name: String): File =
        File(context.cacheDir, "reports").apply { mkdirs() }.resolve(name)

    private fun share(context: Context, file: File, mime: String, subject: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Exportar reporte").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun String.csv(): String = "\"" + replace("\"", "\"\"") + "\""
}
