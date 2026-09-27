package com.cognitech.mindflow.data.local

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("mindflow_session", Context.MODE_PRIVATE)

    val currentUserId: Long?
        get() = prefs.getLong(KEY_USER_ID, NO_USER).takeIf { it != NO_USER }

    fun login(userId: Long) = prefs.edit { putLong(KEY_USER_ID, userId) }

    fun logout() = prefs.edit { remove(KEY_USER_ID) }

    var pinLock: Boolean
        get() = prefs.getBoolean(KEY_PIN, true)
        set(value) = prefs.edit { putBoolean(KEY_PIN, value) }

    var darkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK, false)
        set(value) = prefs.edit { putBoolean(KEY_DARK, value) }

    var habitReminders: Boolean
        get() = prefs.getBoolean(KEY_REMINDERS, true)
        set(value) = prefs.edit { putBoolean(KEY_REMINDERS, value) }

    private companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_PIN = "pin_lock"
        const val KEY_DARK = "dark_mode"
        const val KEY_REMINDERS = "habit_reminders"
        const val NO_USER = -1L
    }
}
