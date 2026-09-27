package com.cognitech.mindflow.data.repository

import android.content.ContentValues
import com.cognitech.mindflow.data.local.MindFlowDatabase
import com.cognitech.mindflow.data.local.MindFlowDatabase.Companion.TABLE_HABITS
import com.cognitech.mindflow.data.local.MindFlowDatabase.Companion.TABLE_HABIT_LOGS
import com.cognitech.mindflow.data.model.Habit
import com.cognitech.mindflow.data.model.HabitCategories
import com.cognitech.mindflow.data.model.HabitLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HabitRepository(private val database: MindFlowDatabase) {

    /** Rutinas iniciales de cada cuenta nueva (las del mockup de Hábitos). */
    fun seedDefaults(userId: Long) {
        listOf(
            "Beber 2L de agua" to HabitCategories.PHYSICAL,
            "Pausa activa de 5 min" to HabitCategories.WELLNESS,
            "Desconexión digital (9PM)" to HabitCategories.SLEEP,
            "Meditar 10 minutos" to HabitCategories.MENTAL,
            "Estudiar Estadistica (2h)" to HabitCategories.STUDY,
        ).forEach { (name, category) -> insert(userId, name, category, "Diario") }
    }

    suspend fun create(userId: Long, name: String, frequency: String) = withContext(Dispatchers.IO) {
        insert(userId, name.trim(), HabitCategories.infer(name), frequency)
    }

    suspend fun listByUser(userId: Long): List<Habit> = withContext(Dispatchers.IO) {
        val today = LocalDate.now()
        val rows = database.readableDatabase.query(
            TABLE_HABITS, arrayOf("id", "name", "category", "frequency", "created_at"),
            "user_id = ?", arrayOf(userId.toString()), null, null, "created_at ASC, id ASC",
        ).use { c ->
            buildList { while (c.moveToNext()) add(listOf(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getLong(4))) }
        }
        rows.map { r ->
            val id = r[0] as Long
            val dates = completionDates(id)
            Habit(
                id = id,
                userId = userId,
                name = r[1] as String,
                category = r[2] as String,
                frequency = r[3] as String,
                createdAt = r[4] as Long,
                doneToday = today in dates,
                streak = streak(dates, today),
            )
        }
    }

    suspend fun toggleToday(habitId: Long) = withContext(Dispatchers.IO) {
        val today = LocalDate.now().toString()
        val db = database.writableDatabase
        val deleted = db.delete(TABLE_HABIT_LOGS, "habit_id = ? AND date = ?", arrayOf(habitId.toString(), today))
        if (deleted == 0) {
            db.insert(TABLE_HABIT_LOGS, null, ContentValues().apply {
                put("habit_id", habitId)
                put("date", today)
            })
        }
    }

    suspend fun history(userId: Long, limit: Int = 30): List<HabitLog> = withContext(Dispatchers.IO) {
        database.readableDatabase.rawQuery(
            """
            SELECT h.name, h.category, l.date FROM $TABLE_HABIT_LOGS l
            JOIN $TABLE_HABITS h ON h.id = l.habit_id
            WHERE h.user_id = ? ORDER BY l.date DESC, l.id DESC LIMIT $limit
            """.trimIndent(),
            arrayOf(userId.toString()),
        ).use { c -> buildList { while (c.moveToNext()) add(HabitLog(c.getString(0), c.getString(1), c.getString(2))) } }
    }

    private fun insert(userId: Long, name: String, category: String, frequency: String): Long =
        database.writableDatabase.insertOrThrow(TABLE_HABITS, null, ContentValues().apply {
            put("user_id", userId)
            put("name", name)
            put("category", category)
            put("frequency", frequency)
            put("created_at", System.currentTimeMillis())
        })

    private fun completionDates(habitId: Long): Set<LocalDate> = database.readableDatabase.query(
        TABLE_HABIT_LOGS, arrayOf("date"), "habit_id = ?", arrayOf(habitId.toString()), null, null, null,
    ).use { c -> buildSet { while (c.moveToNext()) add(LocalDate.parse(c.getString(0))) } }

    // Días consecutivos completados, contando hasta hoy (o hasta ayer si hoy aún no se marcó).
    private fun streak(dates: Set<LocalDate>, today: LocalDate): Int {
        var day = if (today in dates) today else today.minusDays(1)
        var count = 0
        while (day in dates) {
            count++
            day = day.minusDays(1)
        }
        return count
    }
}
