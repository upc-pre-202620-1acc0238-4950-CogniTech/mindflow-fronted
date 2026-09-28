package com.cognitech.mindflow.infrastructure.adapter

import com.cognitech.mindflow.data.local.SessionManager
import com.cognitech.mindflow.domain.port.PreferencesPort
import com.cognitech.mindflow.domain.port.SessionPort

class AndroidSessionAdapter(private val source: SessionManager) : SessionPort {
    override fun isLoggedIn() = source.currentUserId != null
    override fun login(userId: Long) = source.login(userId)
    override fun logout() = source.logout()
    override fun currentUserId() = source.currentUserId
}
class AndroidPreferencesAdapter(private val source: SessionManager) : PreferencesPort {
    override var pinLock: Boolean
        get() = source.pinLock
        set(value) { source.pinLock = value }
    override var darkMode: Boolean
        get() = source.darkMode
        set(value) { source.darkMode = value }
    override var habitReminders: Boolean
        get() = source.habitReminders
        set(value) { source.habitReminders = value }
}
