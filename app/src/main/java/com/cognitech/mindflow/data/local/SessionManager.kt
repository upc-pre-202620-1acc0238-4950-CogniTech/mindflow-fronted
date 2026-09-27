package com.cognitech.mindflow.data.local

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("mindflow_session", Context.MODE_PRIVATE)

    val currentUserId: Long?
        get() = prefs.getLong(KEY_USER_ID, NO_USER).takeIf { it != NO_USER }

    fun login(userId: Long) = prefs.edit { putLong(KEY_USER_ID, userId) }

    fun logout() = prefs.edit { remove(KEY_USER_ID) }

    private companion object {
        const val KEY_USER_ID = "user_id"
        const val NO_USER = -1L
    }
}
