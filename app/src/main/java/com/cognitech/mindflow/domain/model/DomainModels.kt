package com.cognitech.mindflow.domain.model

import java.time.LocalDate

data class User(val id: Long, val email: String, val name: String, val occupation: String, val timezone: String, val plan: String, val createdAt: Long) {
    val isPremium get() = plan == PLAN_PREMIUM
    val initial get() = name.trim().take(1).uppercase().ifBlank { "U" }
    companion object { const val PLAN_FREEMIUM = "Freemium"; const val PLAN_PREMIUM = "Premium" }
}

data class JournalEntry(val id: Long, val userId: Long, val title: String, val content: String, val category: String, val sentiment: Sentiment, val aiResponse: String?, val createdAt: Long)
enum class Sentiment { POSITIVE, NEUTRAL, NEGATIVE;
    fun label() = when (this) { POSITIVE -> "Positivo"; NEGATIVE -> "Negativo"; NEUTRAL -> "Neutral" }
    companion object { fun fromStorage(value: String) = entries.firstOrNull { it.name.equals(value, true) } ?: NEUTRAL }
}

data class Habit(val id: Long, val userId: Long, val name: String, val category: HabitCategory, val frequency: HabitFrequency, val createdAt: Long, val doneToday: Boolean, val streak: Int)
data class HabitLog(val habitName: String, val category: HabitCategory, val date: LocalDate)
enum class HabitFrequency(val label: String) { DAILY("Diario"), WEEKLY("Semanal"); companion object { fun fromStorage(value: String) = entries.firstOrNull { it.label == value } ?: DAILY } }
enum class HabitCategory(val label: String) { PHYSICAL("💧 Salud Física"), WELLNESS("🧘 Bienestar"), SLEEP("🌙 Sueño"), MENTAL("🧠 Salud Mental"), STUDY("📚 Estudios") }

data class ChatMessage(val text: String, val fromUser: Boolean)

object JournalCategories { val all = listOf("Estudios", "Trabajo", "Familia", "Reflexión Personal", "Salud") }
