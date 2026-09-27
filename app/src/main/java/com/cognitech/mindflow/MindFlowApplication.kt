package com.cognitech.mindflow

import android.app.Application
import com.cognitech.mindflow.data.ai.LocalAiResponder
import com.cognitech.mindflow.data.local.MindFlowDatabase
import com.cognitech.mindflow.data.local.SessionManager
import com.cognitech.mindflow.data.repository.AuthRepository
import com.cognitech.mindflow.data.repository.HabitRepository
import com.cognitech.mindflow.data.repository.JournalRepository

class MindFlowApplication : Application() {

    lateinit var authRepository: AuthRepository
        private set
    lateinit var journalRepository: JournalRepository
        private set
    lateinit var habitRepository: HabitRepository
        private set
    val aiResponder = LocalAiResponder()

    override fun onCreate() {
        super.onCreate()
        val database = MindFlowDatabase(this)
        habitRepository = HabitRepository(database)
        authRepository = AuthRepository(database, SessionManager(this), habitRepository)
        journalRepository = JournalRepository(database, aiResponder)
    }
}
