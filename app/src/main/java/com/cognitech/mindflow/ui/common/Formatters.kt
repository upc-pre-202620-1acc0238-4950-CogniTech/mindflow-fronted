package com.cognitech.mindflow.ui.common

import com.cognitech.mindflow.data.model.JournalEntry
import com.cognitech.mindflow.data.model.Sentiment
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

val SpanishLocale: Locale = Locale.forLanguageTag("es-PE")

fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

private fun String.capitalized() = replaceFirstChar { it.titlecase(SpanishLocale) }

/** "Jueves, 26 de Abril" */
fun LocalDate.longLabel(): String =
    "${dayOfWeek.getDisplayName(TextStyle.FULL, SpanishLocale).capitalized()}, $dayOfMonth de " +
        month.getDisplayName(TextStyle.FULL, SpanishLocale).capitalized()

/** "Domingo, 26 de Abril, 3:09 AM" */
fun Long.entryDateLabel(): String {
    val time = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    return time.toLocalDate().longLabel() + ", " + DateTimeFormatter.ofPattern("h:mm a", Locale.US).format(time)
}

/** "Hace 3 horas", "Ayer, 9:00 PM", "Lunes, 8:15 AM" */
fun Long.relativeLabel(): String {
    val now = System.currentTimeMillis()
    val minutes = (now - this) / 60_000
    val zoned = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
    val date = zoned.toLocalDate()
    val today = LocalDate.now()
    val time = DateTimeFormatter.ofPattern("h:mm a", Locale.US).format(zoned)
    return when {
        minutes < 1 -> "Justo ahora"
        minutes < 60 -> "Hace $minutes min"
        date == today -> (minutes / 60).let { if (it == 1L) "Hace 1 hora" else "Hace $it horas" }
        date == today.minusDays(1) -> "Ayer, $time"
        date.isAfter(today.minusDays(7)) ->
            date.dayOfWeek.getDisplayName(TextStyle.FULL, SpanishLocale).capitalized() + ", " + time
        else -> "${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.SHORT, SpanishLocale)}, $time"
    }
}

fun monthYearLabel(date: LocalDate): String =
    date.month.getDisplayName(TextStyle.FULL, SpanishLocale).capitalized() + " " + date.year

/** Puntaje de ánimo: positivo 1.0, neutral 0.5, negativo 0.25. */
fun sentimentScore(sentiment: String) = when (sentiment) {
    Sentiment.POSITIVE -> 1f
    Sentiment.NEGATIVE -> 0.25f
    else -> 0.5f
}

/** Sentimiento predominante de un grupo de entradas. */
fun List<JournalEntry>.dominantSentiment(): String? {
    if (isEmpty()) return null
    val avg = map { sentimentScore(it.sentiment) }.average()
    return when {
        avg >= 0.75 -> Sentiment.POSITIVE
        avg >= 0.45 -> Sentiment.NEUTRAL
        else -> Sentiment.NEGATIVE
    }
}

/** Lunes de la semana de [date]. */
fun weekStart(date: LocalDate = LocalDate.now()): LocalDate = date.minusDays((date.dayOfWeek.value - 1).toLong())
