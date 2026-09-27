package com.cognitech.mindflow.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MindFlowDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL UNIQUE,
                name TEXT NOT NULL,
                password_hash TEXT NOT NULL,
                occupation TEXT NOT NULL DEFAULT '',
                timezone TEXT NOT NULL DEFAULT 'GMT-5 (Lima, Perú)',
                plan TEXT NOT NULL DEFAULT 'Freemium',
                created_at INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE $TABLE_JOURNAL (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL REFERENCES $TABLE_USERS(id) ON DELETE CASCADE,
                title TEXT NOT NULL,
                content TEXT NOT NULL,
                category TEXT NOT NULL,
                sentiment TEXT NOT NULL DEFAULT 'neutral',
                ai_response TEXT,
                created_at INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX idx_journal_user ON $TABLE_JOURNAL(user_id, created_at DESC)")
        db.execSQL(
            """
            CREATE TABLE $TABLE_HABITS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL REFERENCES $TABLE_USERS(id) ON DELETE CASCADE,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                frequency TEXT NOT NULL DEFAULT 'Diario',
                created_at INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE $TABLE_HABIT_LOGS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                habit_id INTEGER NOT NULL REFERENCES $TABLE_HABITS(id) ON DELETE CASCADE,
                date TEXT NOT NULL,
                UNIQUE(habit_id, date)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HABIT_LOGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HABITS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_JOURNAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "mindflow.db"
        const val DATABASE_VERSION = 2
        const val TABLE_USERS = "users"
        const val TABLE_JOURNAL = "journal_entries"
        const val TABLE_HABITS = "habits"
        const val TABLE_HABIT_LOGS = "habit_logs"
    }
}
