package com.cognitech.mindflow.data.model

/** Espejo local de la entidad User del backend (iam). */
data class User(
    val id: Long,
    val email: String,
    val name: String,
    val occupation: String,
    val timezone: String,
    val plan: String,
    val createdAt: Long,
) {
    val isPremium: Boolean get() = plan == PLAN_PREMIUM
    val initial: String get() = name.trim().take(1).uppercase().ifBlank { "U" }

    companion object {
        const val PLAN_FREEMIUM = "Freemium"
        const val PLAN_PREMIUM = "Premium"
    }
}

/** Espejo local de la entidad JournalEntry del backend (journal). */
data class JournalEntry(
    val id: Long,
    val userId: Long,
    val title: String,
    val content: String,
    val category: String,
    val sentiment: String,
    val aiResponse: String?,
    val createdAt: Long,
)

/** Espejo local del agregado Habit del backend (habits). */
data class Habit(
    val id: Long,
    val userId: Long,
    val name: String,
    val category: String,
    val frequency: String,
    val createdAt: Long,
    val doneToday: Boolean,
    val streak: Int,
)

/** Espejo local de HabitCompletionLog del backend. */
data class HabitLog(
    val habitName: String,
    val category: String,
    val date: String,
)

object Sentiment {
    const val POSITIVE = "positive"
    const val NEUTRAL = "neutral"
    const val NEGATIVE = "negative"

    fun label(value: String) = when (value) {
        POSITIVE -> "Positivo"
        NEGATIVE -> "Negativo"
        else -> "Neutral"
    }
}

val JournalCategories = listOf("Estudios", "Trabajo", "Familia", "Reflexión Personal", "Salud")
val JournalFilterChips = listOf("Todos", "Trabajo", "Estudios", "Familia")
val HabitFrequencies = listOf("Diario", "Semanal")

object HabitCategories {
    const val PHYSICAL = "💧 Salud Física"
    const val WELLNESS = "🧘 Bienestar"
    const val SLEEP = "🌙 Sueño"
    const val MENTAL = "🧠 Salud Mental"
    const val STUDY = "📚 Estudios"

    fun infer(name: String): String {
        val n = name.lowercase()
        return when {
            listOf("agua", "ejercicio", "caminar", "correr", "gym", "comer").any { it in n } -> PHYSICAL
            listOf("dormir", "sueño", "desconex", "pantalla", "noche").any { it in n } -> SLEEP
            listOf("medit", "respira", "journal", "diario", "gratitud").any { it in n } -> MENTAL
            listOf("estudi", "leer", "curso", "tarea", "clase").any { it in n } -> STUDY
            else -> WELLNESS
        }
    }
}
