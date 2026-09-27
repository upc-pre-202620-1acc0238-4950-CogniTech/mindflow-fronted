package com.cognitech.mindflow.data.repository

import android.content.ContentValues
import com.cognitech.mindflow.data.ai.LocalAiResponder
import com.cognitech.mindflow.data.local.MindFlowDatabase
import com.cognitech.mindflow.data.local.MindFlowDatabase.Companion.TABLE_JOURNAL
import com.cognitech.mindflow.data.model.JournalEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JournalRepository(
    private val database: MindFlowDatabase,
    private val aiResponder: LocalAiResponder,
) {

    suspend fun create(userId: Long, content: String, category: String): JournalEntry =
        withContext(Dispatchers.IO) {
            val text = content.trim()
            val sentiment = aiResponder.detectSentiment(text)
            val aiResponse = aiResponder.respond(text, sentiment)
            val title = aiResponder.title(text)
            val now = System.currentTimeMillis()
            val values = ContentValues().apply {
                put("user_id", userId)
                put("title", title)
                put("content", text)
                put("category", category)
                put("sentiment", sentiment)
                put("ai_response", aiResponse)
                put("created_at", now)
            }
            val id = database.writableDatabase.insertOrThrow(TABLE_JOURNAL, null, values)
            JournalEntry(id, userId, title, text, category, sentiment, aiResponse, now)
        }

    suspend fun listByUser(userId: Long): List<JournalEntry> = withContext(Dispatchers.IO) {
        database.readableDatabase.query(
            TABLE_JOURNAL,
            arrayOf("id", "user_id", "title", "content", "category", "sentiment", "ai_response", "created_at"),
            "user_id = ?", arrayOf(userId.toString()), null, null, "created_at DESC",
        ).use { c ->
            buildList {
                while (c.moveToNext()) {
                    add(
                        JournalEntry(
                            id = c.getLong(0),
                            userId = c.getLong(1),
                            title = c.getString(2),
                            content = c.getString(3),
                            category = c.getString(4),
                            sentiment = c.getString(5),
                            aiResponse = if (c.isNull(6)) null else c.getString(6),
                            createdAt = c.getLong(7),
                        )
                    )
                }
            }
        }
    }
}
