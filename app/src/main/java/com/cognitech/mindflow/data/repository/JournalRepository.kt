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
            val sentiment = aiResponder.detectSentiment(content)
            val aiResponse = aiResponder.respond(content, sentiment)
            val now = System.currentTimeMillis()
            val values = ContentValues().apply {
                put("user_id", userId)
                put("content", content.trim())
                put("category", category)
                put("sentiment", sentiment)
                put("ai_response", aiResponse)
                put("created_at", now)
            }
            val id = database.writableDatabase.insertOrThrow(TABLE_JOURNAL, null, values)
            JournalEntry(id, userId, content.trim(), category, sentiment, aiResponse, now)
        }

    suspend fun listByUser(userId: Long): List<JournalEntry> = withContext(Dispatchers.IO) {
        database.readableDatabase.query(
            TABLE_JOURNAL,
            arrayOf("id", "user_id", "content", "category", "sentiment", "ai_response", "created_at"),
            "user_id = ?",
            arrayOf(userId.toString()),
            null, null,
            "created_at DESC",
        ).use { c ->
            buildList {
                while (c.moveToNext()) {
                    add(
                        JournalEntry(
                            id = c.getLong(0),
                            userId = c.getLong(1),
                            content = c.getString(2),
                            category = c.getString(3),
                            sentiment = c.getString(4),
                            aiResponse = if (c.isNull(5)) null else c.getString(5),
                            createdAt = c.getLong(6),
                        )
                    )
                }
            }
        }
    }
}
