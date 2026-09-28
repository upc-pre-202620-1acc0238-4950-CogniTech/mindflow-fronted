package com.cognitech.mindflow.domain.service

import com.cognitech.mindflow.domain.model.*
import java.time.LocalDate

object HabitPolicy {
    fun categoryFor(name: String): HabitCategory { val n = name.lowercase(); return when { listOf("agua", "ejercicio", "caminar", "correr", "gym", "comer").any(n::contains) -> HabitCategory.PHYSICAL; listOf("dormir", "sueño", "desconex", "pantalla", "noche").any(n::contains) -> HabitCategory.SLEEP; listOf("medit", "respira", "journal", "diario", "gratitud").any(n::contains) -> HabitCategory.MENTAL; listOf("estudi", "leer", "curso", "tarea", "clase").any(n::contains) -> HabitCategory.STUDY; else -> HabitCategory.WELLNESS } }
    fun streak(dates: Set<LocalDate>, today: LocalDate): Int { var day = if (today in dates) today else today.minusDays(1); var count = 0; while (day in dates) { count++; day = day.minusDays(1) }; return count }
    fun stressDetected(entries: List<JournalEntry>) = entries.take(3).count { it.sentiment == Sentiment.NEGATIVE } >= 2
    fun isPaused(habit: Habit, stressed: Boolean) = stressed && habit.category == HabitCategory.STUDY
}
